plugins {
    java
    `maven-publish`
    kotlin("jvm") version "2.0.0"
    kotlin("plugin.lombok") version "2.0.0"
}

val logback_version: String by project
val jackson_version: String by project
val lombok_version: String by project
val fastjson_version: String by project
val hutool_version: String by project

allprojects {
    apply(plugin = "java")
    apply(plugin = "maven-publish")
    apply(plugin = "org.jetbrains.kotlin.jvm")

    group = rootProject.group
    version = rootProject.version

    repositories {
        mavenCentral()
    }

    kotlin {
        jvmToolchain(21)
    }

}

subprojects {
    dependencies {
        testImplementation(kotlin("test"))
        implementation("ch.qos.logback:logback-classic:$logback_version")
        implementation("com.alibaba.fastjson2:fastjson2:$fastjson_version")
        implementation("cn.hutool:hutool-all:$hutool_version")
    }

    tasks.test {
        useJUnitPlatform()
    }

}
