rootProject.name = "akotlinator"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
        repositories {
                google()
                gradlePluginPortal()
                mavenCentral()
        }
}

dependencyResolutionManagement {
        repositories {
                google()
                mavenCentral()
        }
        versionCatalogs {
                create("libs") {
                        plugin("kotlinJvm", "org.jetbrains.kotlin.jvm").version("2.0.0-Beta1")
                        plugin("androidApplication", "com.android.application").version("8.1.0")
                        plugin("androidLibrary", "com.android.library").version("8.1.0")
                        plugin("kotlinMultiplatform", "org.jetbrains.kotlin.multiplatform").version("2.0.0-Beta1")
                        version("android.compileSdk", "30")
                        version("android.minSdk", "21")
                }
        }
}

include(":shared")
