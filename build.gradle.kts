plugins {
    kotlin("jvm") version "1.8.21"
    kotlin("plugin.serialization") version "1.8.21"
    application
    id("com.github.ben-manes.versions") version "0.46.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()


}

dependencies {
    testImplementation(kotlin("test"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

    //KTOR AND SERIALIZATION
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
    implementation("io.ktor:ktor-client-core:2.2.4")
    implementation("io.ktor:ktor-client-cio:2.2.4")
    implementation("io.ktor:ktor-client-content-negotiation:2.2.4")
    implementation("io.ktor:ktor-serialization-kotlinx-xml:2.2.4")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.2.4")
    implementation("com.jcabi:jcabi-xml:0.27.2")

    //TEMPLATE ENGINE (FOR TRANSLATING STRINGS)
    implementation("dev.icerock.moko:resources:0.22.0")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}

application {
    mainClass.set("MainKt")
}