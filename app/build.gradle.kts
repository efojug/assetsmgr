@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
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
        targetSdk = 31
        versionCode = generateVersionCode()
        versionName = "3.0"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
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
    implementation(libs.lottie.compose)

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.1")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.1")
    implementation("androidx.preference:preference-ktx:1.2.1")
    implementation("androidx.compose.material:material-android:1.5.0")

    implementation("dev.rikka.rikkax.material:material:2.7.0")
    implementation("dev.rikka.rikkax.material:material-preference:2.0.0")
}

configurations.all {
    exclude("androidx.appcompat", "appcompat")
}