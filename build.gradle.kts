plugins {
    kotlin("multiplatform") version "2.0.0-Beta1"
    embeddedKotlin("plugin.serialization")
    application
}



group = "org.example"
version = "1.0-SNAPSHOT"

application {
	mainClass.set("MainKt")
}

repositories {
    mavenCentral()
    maven("https://jitpack.io/")
    maven("https://plugins.gradle.org/m2/")
}

dependencies {
    testImplementation(kotlin("test"))
	testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")

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

	//DE-COROUTINATOR FOR STACKTRACES
	implementation("dev.reformator.stacktracedecoroutinator:stacktrace-decoroutinator-jvm:2.3.6")

    //TEMPLATE ENGINE (FOR TRANSLATING STRINGS)
    implementation("dev.icerock.moko:resources:0.22.0")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
    androidTarget()
    iosArm64()
    iosSimulatorArm64()
	// The iosMain source set is created automatically

	applyDefaultHierarchyTemplate()
}

application {
    mainClass.set("MainKt")
}
