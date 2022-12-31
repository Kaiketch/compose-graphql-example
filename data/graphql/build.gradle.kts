import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

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

    buildTypes {
        val token = gradleLocalProperties(rootDir).getProperty("git_token")

        release {
            buildConfigField("String", "GIT_TOKEN", "\"$token\"")
        }

        debug {
            buildConfigField("String", "GIT_TOKEN", "\"$token\"")
        }
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
