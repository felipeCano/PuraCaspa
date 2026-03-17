import java.util.Properties

val secretProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) {
        file.inputStream().use { load(it) }
    }
}
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("kapt")
    alias(libs.plugins.google.dagger.hilt)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.pura.caspa"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.pura.caspa"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        val appId = secretProperties.getProperty("ADMOB_APP_ID") ?: ""
        val adUnitId = secretProperties.getProperty("ADMOB_REWARDED_UNIT_ID") ?: ""
        val adBannerUnitId = secretProperties.getProperty("ADMOB_BANNER_UNIT_ID") ?: ""

        manifestPlaceholders["ADMOB_APP_ID"] = appId
        buildConfigField("String", "ADMOB_REWARDED_UNIT_ID", "\"$adUnitId\"")
        buildConfigField("String", "ADMOB_BANNER_UNIT_ID", "\"$adBannerUnitId\"")
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    //implementation(libs.ads.mobile.sdk)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    //Gson
    implementation(libs.gson)
    //Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.play.services)
    // ViewModel para Android
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    // Import the BoM for the Firebase platform
    implementation(platform(libs.firebase.bom))
    // Cloud Firestore library
    implementation(libs.firebase.firestore)
    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    // ViewModel utilities for Compose
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    // LiveData
    implementation(libs.androidx.lifecycle.livedata.ktx)
    // Annotation processor
    kapt(libs.androidx.lifecycle.compiler)
    //hilt
    implementation(libs.google.dagger.hilt)
    kapt(libs.google.dagger.hilt.compiler)
    //Compose + Hilt
    implementation(libs.androidx.hilt.navigation.compose)
    //Navigation Compose
    implementation(libs.androidx.navigation.compose)
    //FirebaseInstallation
    implementation(libs.firebase.installations.ktx)
    //
    implementation(libs.play.services.ads)
}

kapt {
    correctErrorTypes = true
}
