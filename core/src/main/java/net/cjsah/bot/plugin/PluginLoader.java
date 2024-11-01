package net.cjsah.bot.plugin;

import com.alibaba.fastjson2.JSONObject;
import net.cjsah.bot.FilePaths;
import net.cjsah.bot.command.CommandManager;
import net.cjsah.bot.event.EventManager;
import net.cjsah.bot.resolver.Counter;
import net.cjsah.bot.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginLoader extends URLClassLoader {
    private static final Logger log = LoggerFactory.getLogger("PluginLoader");

    public PluginLoader(File file) throws MalformedURLException {
        super(new URL[]{file.toURI().toURL()});
    }

    public static void loadPlugins() throws InterruptedException {
        Counter counter = new Counter();
        PluginContext.appendPlugin(MainPlugin.INSTANCE, MainPlugin.PLUGIN_INFO, null);
        PluginThreadPools.execute(MainPlugin.PLUGIN_INFO.getId(), () -> {
            counter.increment();
            log.info("正在加载核心插件");
            PluginContext.PLUGIN_INFO.set(MainPlugin.PLUGIN_INFO);
            MainPlugin.INSTANCE.onLoad();
            log.info("核心插件加载完成");
            counter.completed();
        });
        counter.await();
        List<File> jars = PluginLoader.getPluginJars();
        if (jars.isEmpty()) {
            log.info("没有插件被加载");
            return;
        }
        log.info("正在加载 {} 个插件", jars.size());
        for (File jar : jars) {
            try {
                PluginLoader loader = new PluginLoader(jar);
                JarFile jarFile = new JarFile(jar);
                JSONObject json = PluginLoader.readJarPluginInfo(jarFile);
                PluginInfo info = new PluginInfo(json);
                if (PluginLoader.checkPluginExist(info, jar.getName())) {
                    continue;
                }
                String main = json.getString("main");
                Class<?> clazz = loader.loadClass(main);
                if (!Plugin.class.isAssignableFrom(clazz)) {
                    log.error("插件 {} 的主类 {} 不是插件类", info.getId(), main);
                    continue;
                }
                Plugin plugin = (Plugin) clazz.getDeclaredConstructor().newInstance();
                counter.increment();
                PluginContext.appendPlugin(plugin, info, loader);
                PluginThreadPools.execute(info.getId(), () -> {
                    try {
                        PluginContext.PLUGIN_INFO.set(info);
                        plugin.onLoad();
                        log.info("插件 {} {} 已加载", info.getName(), info.getVersion());
                    }catch (Exception e) {
                        PluginContext.removePlugin(info.getId());
                        log.error("插件 {} 卸载失败", info.getId(), e);
                    } finally {
                        counter.completed();
                    }
                });
            } catch (Exception e) {
                log.error("插件 {} 加载失败", jar.getName(), e);
            }
        }
        counter.await();
        log.info("插件已全部加载");
    }

    public static void onStarted() {
        for (PluginContext.PluginData data : PluginContext.PLUGINS.values()) {
            PluginThreadPools.execute(data.info().getId(), () -> data.plugin().onStarted());
        }
    }

    public static void unloadPlugins() {
        PluginContext.PLUGINS.values().forEach(it -> PluginLoader.unloadPlugin(it.info().getId()));
        log.info("已卸载所有插件!");
    }

    public static void unloadPlugin(String pluginId) {
        PluginThreadPools.execute(pluginId, () -> {
            Plugin plugin = PluginContext.getCurrentPlugin();
            EventManager.unsubscribe(pluginId);
            CommandManager.deregister(pluginId);
            plugin.onUnload();
        });
        PluginThreadPools.unloadPlugin(pluginId);
    }

    private static List<File> getPluginJars() {
        File[] files = FilePaths.PLUGIN.toFile().listFiles();
        if (files == null) return List.of();
        return Arrays.stream(files).filter(it -> {
            if (!it.isFile()) return false;
            String name = it.getName();
            int index = name.lastIndexOf(".");
            if (index == -1) return false;
            return "jar".equalsIgnoreCase(name.substring(index + 1));
        }).toList();
    }

    private static JSONObject readJarPluginInfo(JarFile jar) {
        JarEntry entry = jar.getJarEntry("plugin.json");
        try (
                InputStream is = jar.getInputStream(entry);
                InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8)
        ) {
            return JsonUtil.deserialize(reader);
        } catch (IOException e) {
            return new JSONObject();
        }
    }

    private static boolean checkPluginExist(PluginInfo info, String jar) {
        PluginContext.PluginData pluginData = PluginContext.PLUGINS.get(info.getId());
        if (pluginData != null) {
            PluginInfo alreadyInfo = pluginData.info();
            log.warn("插件 {} [{} ({}) v{}] 无法加载, 已有同名插件 [{} ({}) v{}]", jar,
                    info.getName(), info.getId(), info.getVersion(),
                    alreadyInfo.getName(), alreadyInfo.getId(), alreadyInfo.getVersion()
            );
            return true;
        }
        return false;
    }
}
