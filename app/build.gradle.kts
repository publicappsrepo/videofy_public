import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.compose.compiler)
    alias(libs.plugins.room)
    alias(libs.plugins.aboutlibraries)
    alias(libs.plugins.kotlinx.serialization)
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")

}
android {
    namespace = "com.appsease.videofy_videoplayer"

    // Add this packaging block
    packaging {
        resources {
            excludes += listOf(
                "META-INF/versions/9/OSGI-INF/MANIFEST.MF"
            )
        }
    }

    signingConfigs {
        create("config") {
            storeFile = file("/Users/akshayvadchhakgmail.com/Desktop/Safe Project/jks/videofy.jks")
            storePassword = "android"
            keyAlias = "android"
            keyPassword = "android"
        }
    }


    compileSdk = 36

    defaultConfig {
        applicationId = "com.appsease.videofy_videoplayer"
        minSdk = 26
        targetSdk = 36
//        versionCode = 1
//        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("debug") {
            isDebuggable = true
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            isShrinkResources = false
            versionNameSuffix = ".debug"

            // Disable automatic build ID generation
            ext["alwaysUpdateBuildId"] = false

            // Disable PNG crunching
            isCrunchPngs = false

            externalNativeBuild {
                cmake {
                    cppFlags += "-DDEBUG"
                    abiFilters += listOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
                }
            }
        }

        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            isShrinkResources = true

            externalNativeBuild {
                cmake {
                    cppFlags += "-DRELEASE"
                    abiFilters += listOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
                }
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
    buildFeatures {
        compose = true
        buildConfig = true
        viewBinding = true
        dataBinding = true
    }
    flavorDimensions += "default"

    productFlavors {
        create("videofy") {
            applicationId = "com.appsease.videofy_videoplayer"
            manifestPlaceholders["app_content_provider"] = "com.appsease.videofy_videoplayer"
            versionCode = 1
            versionName = "1.0"
            dimension = "default"
            signingConfig = signingConfigs.getByName("config")

            // Equivalent of Groovy's setProperty("archivesBaseName", ...)
            setProperty("archivesBaseName", "$versionName.$versionCode")
        }
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll(
            "-Xwhen-guards",
            "-Xcontext-parameters",
            "-Xannotation-default-target=param-property",
            "-opt-in=com.google.accompanist.permissions.ExperimentalPermissionsApi",
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
        )
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.tooling)
    implementation(libs.bundles.compose.navigation3)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.compose.constraintlayout)
    implementation(libs.androidx.material3.icons.extended)
    implementation(libs.androidx.compose.animation.graphics)
    implementation(libs.material)
    implementation(libs.mediasession)
    implementation(libs.androidx.preferences.ktx)
    implementation(libs.androidx.documentfile)
    implementation(libs.saveable)


    implementation(platform(libs.koin.bom))
    implementation(libs.bundles.koin)

    implementation(libs.seeker)
    implementation(libs.compose.prefs)
    implementation(libs.aboutlibraries.compose.m3)

    implementation(libs.accompanist.permissions)

    implementation(libs.room.runtime)
    ksp(libs.room.compiler)
    implementation(libs.room.ktx)

    implementation(libs.kotlinx.immutable.collections)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)

    implementation(libs.truetype.parser)
    implementation(libs.fsaf)
    implementation(libs.mediainfo.lib)
    implementation(files("libs/videofy.aar"))

    // Network protocol libraries
    implementation(libs.smbj) // SMB/CIFS
    implementation(libs.commons.net) // FTP
    implementation(libs.sardine.android) {
        exclude(group = "xpp3", module = "xpp3")
    }
    implementation(libs.nanohttpd)
    implementation(libs.lazycolumnscrollbar)
    implementation(libs.reorderable)

    implementation(libs.coil.core)
    implementation(libs.coil.compose)

    implementation("androidx.lifecycle:lifecycle-livedata-core:2.8.7")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
}