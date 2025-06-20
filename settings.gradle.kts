rootProject.name = "Hortus"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
                maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
                maven("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental")
                maven( "https://androidx.dev/storage/compose-compiler/repository")
                maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
                maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
                maven("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental")
                maven( "https://androidx.dev/storage/compose-compiler/repository")
                maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev")
            }
        }
        mavenCentral()
    }
}

include(":composeApp")