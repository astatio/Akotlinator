plugins {
        alias(libs.plugins.kotlinMultiplatform)
        alias(libs.plugins.androidLibrary)
}

kotlin {
        iosX64()
        iosArm64()
        iosSimulatorArm64()

        androidTarget { compilations.all { kotlinOptions { jvmTarget = "1.8" } } }
        jvm()

        sourceSets {
                val commonMain by getting {
                        dependencies {
                                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
                                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.5.0")
                                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
                                implementation("io.ktor:ktor-client-core:2.2.4")
                                implementation("io.ktor:ktor-client-cio:2.3.6")
                                implementation("io.ktor:ktor-client-content-negotiation:2.2.4")
                                implementation("io.ktor:ktor-serialization-kotlinx-xml:2.3.6")
                                implementation("io.ktor:ktor-serialization-kotlinx-json:2.2.4")
                                implementation("com.jcabi:jcabi-xml:0.27.2")
                        }
                }
                val commonTest by getting {
                        dependencies {
                                implementation(kotlin("test-common"))
                                implementation(kotlin("test-annotations-common"))
                        }
                }
        }
}

android {
        namespace = "org.example.project.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        defaultConfig { minSdk = libs.versions.android.minSdk.get().toInt() }
}
