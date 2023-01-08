@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("kotlin-kapt")
    alias(libs.plugins.kotlin)
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.example.datastore"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
}

dependencies {
    implementation(libs.datastore.preferences)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
}
