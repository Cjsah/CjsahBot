package net.cjsah.bot.plugin

import net.cjsah.bot.FilePaths
import net.cjsah.bot.command.CommandManager
import net.cjsah.bot.event.EventManager
import net.cjsah.bot.resolver.Counter
import net.cjsah.bot.util.JsonUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.io.InputStreamReader
import java.net.URLClassLoader
import java.nio.charset.StandardCharsets
import java.util.jar.JarFile

class PluginLoader(file: File): URLClassLoader(arrayOf(file.toURI().toURL())) {

    companion object {
        @JvmStatic
        private val log: Logger = LoggerFactory.getLogger("PluginLoader")

        @JvmStatic
        fun loadPlugins() {
            val counter = Counter()
            PluginContext.appendPlugin(MainPlugin.INSTANCE, MainPlugin.PLUGIN_INFO, null)
            PluginThreadPools.execute(MainPlugin.INSTANCE) {
                counter.increment()
                PluginContext.PLUGIN_INFO.set(MainPlugin.PLUGIN_INFO)
                MainPlugin.INSTANCE.onLoad()
                counter.completed()
            }
            val files = FilePaths.PLUGIN.toFile().listFiles() ?: emptyArray()
            val jars = files.filter { it.isFile && it.extension == "jar" }
            if (jars.isEmpty()) {
                log.info("没有插件被加载")
                return
            }
            log.info("正在加载 {} 个插件", jars.size)
            for (jar in jars) {
                try {
                    val loader = PluginLoader(jar)
                    val jarFile = JarFile(jar)
                    val infoEntry = jarFile.getJarEntry("plugin.json")
                    val json = jarFile.getInputStream(infoEntry).use { iS ->
                        InputStreamReader(iS, StandardCharsets.UTF_8).use { JsonUtil.deserialize(it) }
                    }
                    val info = PluginInfo(json)
                    if (PluginContext.PLUGINS[info.id]?.let {
                        val alreadyInfo = PluginContext.PLUGINS[it.info.id]!!.info
                        log.warn("插件 {} [{} ({}) v{}] 无法加载, 已有同名插件 [{} ({}) v{}]", jar,
                            info.name, info.id, info.version,
                            alreadyInfo.name, alreadyInfo.id, alreadyInfo.version
                        )
                        true
                    } == true) { break }

                    val main = json.getString("main")
                    val clazz = loader.loadClass(main)
                    val plugin = clazz.getDeclaredConstructor().newInstance() as Plugin
                    counter.increment()
                    PluginContext.appendPlugin(plugin, info, loader)
                    PluginThreadPools.execute(plugin) {
                        try {
                            PluginContext.PLUGIN_INFO.set(info)
                            plugin.onLoad()
                            log.info("插件 {} {} 已加载", info.name, info.version)
                            counter.completed()
                        }catch (e:Exception) {
                            PluginContext.removePlugin(plugin)
                            throw e;
                        }
                    }
                } catch (e: Exception) {
                    log.error("插件 {} 加载失败", jar.name, e)
                }
            }

            counter.await()
            log.info("插件已全部加载")
        }

        @JvmStatic
        fun onStarted() {
            PluginContext.PLUGINS.values.forEach { it ->
                PluginThreadPools.execute(it.plugin) {
                    it.plugin.onStarted()
                }
            }
        }

        @JvmStatic
        fun unloadPlugins() {
            PluginContext.PLUGINS.values.forEach {
                unloadPlugin(it.plugin)
            }
            log.info("已卸载所有插件!")
        }

        @JvmStatic
        fun unloadPlugin(plugin: Plugin) {
            val info = PluginContext.getPluginInfo(plugin);
            PluginThreadPools.execute(plugin) {
                EventManager.unsubscribe(info.id)
                CommandManager.deregister(info.id)
                plugin.onUnload()
            }
            PluginThreadPools.unloadPlugin(plugin)
        }
    }

}