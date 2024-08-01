val ktor_version: String by rootProject
val quartz_version: String by rootProject

dependencies {
//    implementation project(":api")
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-websockets:$ktor_version")
    implementation("org.quartz-scheduler:quartz:$quartz_version")
}
