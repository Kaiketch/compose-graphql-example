@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("kotlin-kapt")
    alias(libs.plugins.kotlin)
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.example.repository"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
}

dependencies {
    api(project(":data:graphql"))

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
}
