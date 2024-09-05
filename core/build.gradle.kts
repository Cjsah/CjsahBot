plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
//    id("org.springframework.boot") version "3.3.2"
}

val ktor_version: String by rootProject
val quartz_version: String by rootProject
val websocket_version: String by rootProject

dependencies {
    implementation(project(":api"))
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-websockets:$ktor_version")
    implementation("org.quartz-scheduler:quartz:$quartz_version")
    implementation("org.java-websocket:Java-WebSocket:$websocket_version")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "net.cjsah.bot.MainKt"
    }
}

tasks.register<JavaExec>("runMain") {
    group = "application"
    description = "Runs the main class"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("net.cjsah.bot.MainKt") // 对于 Kotlin 文件，通常需要使用 MainKt 作为主类名称

    val runDir = rootProject.file("run")

    // 在任务执行之前检查并创建工作目录
    doFirst {
        if (!runDir.exists()) {
            runDir.mkdirs()
        }
    }

    // 设置运行目录
    workingDir = runDir

    // 如果需要传递 JVM 参数或程序参数，可以在这里添加
    // jvmArgs = listOf("-Xmx512m")
    // args = listOf("arg1", "arg2")
}
