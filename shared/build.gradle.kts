plugins {
        alias(libs.plugins.kotlinMultiplatform)
        alias(libs.plugins.androidLibrary)
}

kotlin {
        listOf(
                iosX64(),
                iosArm64(),
                iosSimulatorArm64()
        ).forEach { iosTarget ->
                iosTarget.binaries.framework {
                        baseName = "Shared"
                        isStatic = true
                }
        }

        androidTarget {
                compilations.all {
                        kotlinOptions {
                                jvmTarget = "1.8"
                        }
                }
        }

        sourceSets {
                commonMain.dependencies {
                        implementation(libs.kotlinx.coroutines.core)
                        implementation(libs.kotlinx.serialization.core)
                        implementation(libs.kotlinx.serialization.json)
                        implementation(libs.ktor.client.core)
                        implementation(libs.ktor.client.cio)
                        implementation(libs.ktor.client.content.negotiation)
                        implementation(libs.ktor.serialization.kotlinx.xml)
                        implementation(libs.ktor.serialization.kotlinx.json)
                        implementation(libs.jcabi.xml)
                }
                commonTest.dependencies {
                        implementation(kotlin("test"))
                }
        }
}

android {
        namespace = "org.example.project.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        defaultConfig {
                minSdk = libs.versions.android.minSdk.get().toInt()
        }
}
