plugins {
    kotlin("jvm") version "2.0.0"
    kotlin("plugin.lombok") version "2.0.0"
}

group = "net.cjsah.bot"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val ktor_version: String by project
val logback_version: String by project
val jackson_version: String by project
val lombok_version: String by project
val fastjson_version: String by project
val hutool_version: String by project

dependencies {
    testImplementation(kotlin("test"))
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-websockets:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("com.alibaba.fastjson2:fastjson2:$fastjson_version")
    implementation("cn.hutool:hutool-core:$hutool_version")

    compileOnly("org.projectlombok:lombok:$lombok_version")
    annotationProcessor("org.projectlombok:lombok:$lombok_version")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}
