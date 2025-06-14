import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.kspCompose)
    alias(libs.plugins.room)
    alias(libs.plugins.googleServices)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    sourceSets {
        
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
            implementation(libs.ktor.client.android)
            implementation(libs.accompanist.permissions)

            implementation(project.dependencies.platform(libs.firebase.bom))
            implementation(libs.firebase.vertexai)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)

            implementation(libs.voyager.navigator)
            implementation(libs.voyager.screen.model)
            implementation(libs.voyager.transitions)
            implementation(libs.voyager.koin)
            
            implementation(libs.koin.core)
            implementation(libs.stately.common)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)

            implementation(libs.room.runtime)
            implementation(libs.sqlite.bundled)

            implementation(libs.peekaboo.ui)

            implementation(libs.peekaboo.image.picker)

            implementation(libs.coil.compose.core)
            implementation(libs.coil.compose)
            implementation(libs.coil.mp)

            // Geocoding
            implementation(libs.compass.geocoder)
            implementation(libs.compass.geocoder.mobile)

            // Geolocation
            implementation(libs.compass.geolocation)
            implementation(libs.compass.geolocation.mobile)
            implementation(libs.compass.autocomplete.mobile)
            implementation(libs.compass.autocomplete)

            implementation(libs.compose.shimmer)

            implementation(libs.compottie)
            implementation(libs.compottie.dot)
            implementation(libs.compottie.resources)

            implementation(libs.kotlinx.datetime)

            implementation(libs.multiplatform.settings.no.arg)
            implementation(libs.multiplatform.settings.coroutines)

            implementation(libs.firebase.auth)

        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}

android {
    namespace = "org.alba.hortus"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.alba.hortus"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    androidResources {
        generateLocaleConfig = true
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    implementation(libs.androidx.material3.android)
    debugImplementation(compose.uiTooling)
    ksp(libs.room.compiler)
}

