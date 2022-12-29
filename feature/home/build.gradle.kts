plugins {
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")

    alias(libs.plugins.kotlin)
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.example.home"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.get()
    }
}

dependencies {
    implementation(libs.bundles.compose)
    implementation(libs.androidx.core)
    implementation(libs.androidx.lifecycle)
    implementation(libs.androidx.activity)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
}