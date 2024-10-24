package net.cjsah.bot.launcher.archive;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.security.ProtectionDomain;

public class ArchiveLauncher {
    private final JarFileArchive archive;

    public ArchiveLauncher() {
        try {
            this.archive = this.createArchive();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private JarFileArchive createArchive() throws URISyntaxException {
        ProtectionDomain protectionDomain = getClass().getProtectionDomain();
        CodeSource codeSource = protectionDomain.getCodeSource();
        URI location = (codeSource == null ? null : codeSource.getLocation().toURI());
        String path = (location == null ? null : location.getSchemeSpecificPart());
        if (path == null) {
            throw new IllegalStateException("Unable to determine code source archive");
        }
        File root = new File(path);
        if (!root.exists() || root.isDirectory()) {
            throw new IllegalStateException(
                    "Unable to determine code source archive from " + root);
        }
        return new JarFileArchive(root);
    }

//    protected void launch(String[] args) throws Exception {
//        JarFile.registerUrlProtocolHandler();
//        // 生成自定义ClassLoader
//        ClassLoader classLoader = createClassLoader(getClassPathArchives());
//        // 启动应用
//        launch(args, getMainClass(), classLoader);
//    }
//
//    public static void registerUrlProtocolHandler() {
//        String handlers = System.getProperty(PROTOCOL_HANDLER, "");
//        System.setProperty(PROTOCOL_HANDLER, ("".equals(handlers) ? HANDLERS_PACKAGE
//                : handlers + "|" + HANDLERS_PACKAGE));
//        resetCachedUrlHandlers();
//    }
//    protected void launch(String[] args, String mainClass, ClassLoader classLoader)
//            throws Exception {
//        // 将自定义ClassLoader设置为当前线程上下文类加载器
//        Thread.currentThread().setContextClassLoader(classLoader);
//        // 启动应用
//        createMainMethodRunner(mainClass, args, classLoader).run();
//    }
//
//    protected List<Archive> getClassPathArchives() throws Exception {
//        // 获取/BOOT-INF/lib下所有jar及/BOOT-INF/classes目录对应的archive
//        List<Archive> archives = new ArrayList<>(
//                this.archive.getNestedArchives(this::isNestedArchive));
//        postProcessClassPathArchives(archives);
//        return archives;
//    }

}
