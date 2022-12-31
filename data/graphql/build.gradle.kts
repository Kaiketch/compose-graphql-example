@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("kotlin-kapt")
    alias(libs.plugins.kotlin)
    alias(libs.plugins.android.library)
    alias(libs.plugins.apollo)
}

android {
    namespace = "com.example.graphql"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.get()
    }
}

apollo {
    packageName.set("com.example")
}

dependencies {
    implementation(libs.apollo.runtime)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
}
