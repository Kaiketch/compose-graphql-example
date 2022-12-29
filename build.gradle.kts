buildscript {
    dependencies {
        // [plugins]対応していない場合はとりあえU従来通りclasspath指定で
        classpath(libs.daggerHiltAndroidGradlePlugin)
    }
}

plugins {
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
}