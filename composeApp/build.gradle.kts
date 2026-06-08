import com.android.build.api.dsl.ApplicationExtension
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

val appVersion = "1.1.2"
val appName = "AudioDetectionTool"
val packageName = "com.github.sor2171.audiodetectiontool"

val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) {
        file.inputStream().use { load(it) }
    }
}

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = appName
            isStatic = true
        }
    }

    jvm()

    js {
        browser {
            commonWebpackConfig {
                sourceMaps = false
            }
        }
        binaries.executable()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.material.icons.extended)

            // https://github.com/vinceglb/FileKit
            implementation(libs.filekit.compose)
            // https://github.com/arthenica/ffmpeg-kit
//            implementation(libs.ffmpeg.kit.min)
            // https://github.com/dosier/kodio
            implementation(libs.kodio.core)
            // https://kotlin.github.io/multik/
            implementation(libs.multik.kotlin)
            implementation(libs.slf4j.api)
            implementation(libs.slf4j.simple)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }
}

configure<ApplicationExtension> {
    namespace = packageName
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = packageName
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = appVersion.split(".").joinToString("").toInt()
        versionName = appVersion
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    splits {
        abi {
            isEnable = true
            reset()
            include(
                "armeabi-v7a",
                "arm64-v8a",
                "x86_64"
            )
            isUniversalApk = true
        }
    }
    signingConfigs {
        create("release") {
            storeFile = file(
                localProperties.getProperty("KEYSTORE_FILE")
                    ?: "${rootDir}/release.jks"
            )
            storePassword = System.getenv("ANDROID_KEYSTORE_PASSWORD")
                ?: localProperties.getProperty("ANDROID_KEYSTORE_PASSWORD")
            keyAlias = System.getenv("ANDROID_KEY_ALIAS")
                ?: localProperties.getProperty("ANDROID_KEY_ALIAS")
            keyPassword = System.getenv("ANDROID_KEY_PASSWORD")
                ?: localProperties.getProperty("ANDROID_KEY_PASSWORD")
        }
    }
    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

@Suppress("UnstableApiUsage")
androidComponents {
    onVariants { variant ->
        variant.outputs.forEach { output ->
            val abi = output.filters
                .find { it.filterType.name == "ABI" }
                ?.identifier
                ?: "universal"
            output.outputFileName.set("$appName-$appVersion-$abi.apk")
        }
    }
}

dependencies {
    debugImplementation(libs.compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "$packageName.MainKt"

        buildTypes.release.proguard {
            configurationFiles.from(project.file("proguard-rules.pro"))
        }

        nativeDistributions {
            targetFormats(
                TargetFormat.Msi,    // Windows
                TargetFormat.Exe,    // Windows
                TargetFormat.Dmg,    // macOS
                TargetFormat.Pkg,    // macOS
                TargetFormat.Deb,    // Linux
                TargetFormat.Rpm     // Linux
            )
            packageName = appName
            packageVersion = appVersion

            windows {
                iconFile.set(project.file("icons/icon.ico"))
            }

            macOS {
                iconFile.set(project.file("icons/icon.icns"))
            }

            linux {
                iconFile.set(project.file("icons/icon.png"))
            }
        }
    }
}
