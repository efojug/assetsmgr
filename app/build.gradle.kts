@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp") version "1.9.0-1.0.12"
}

fun generateVersionCode(): Int =
    Runtime.getRuntime().exec("git rev-list --all --count").inputStream.bufferedReader()
        .readLines()[0].toInt()

android {
    namespace = "com.efojug.assetsmgr"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.efojug.assetsmgr"
        minSdk = 30
        targetSdk = 32
        versionCode = generateVersionCode()
        versionName = "2.3"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }
}

kotlin {
    jvmToolchain(8)
}

dependencies {
    implementation(libs.data.store)
    implementation(libs.koin)
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)

    implementation("com.highcapable.yukihookapi:api:1.1.11")
    compileOnly("de.robv.android.xposed:api:82")
    ksp("com.highcapable.yukihookapi:ksp-xposed:1.1.11")
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")
    implementation("androidx.preference:preference:1.2.1")
}