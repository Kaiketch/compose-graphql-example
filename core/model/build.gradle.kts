@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.example.model"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
}

dependencies {
}