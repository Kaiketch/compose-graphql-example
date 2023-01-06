@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("kotlin-kapt")
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
    implementation(project(":feature:common"))
    implementation(project(":data:repository"))

    implementation(libs.bundles.compose)
    implementation(libs.androidx.core)
    implementation(libs.androidx.lifecycle)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.viewmodel.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.bundles.apollo)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
}