// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.dagger.hilt.android.gradle.plugin) apply false
    alias(libs.plugins.navigationSafeArgs) apply false
    alias(libs.plugins.kotlinKsp) apply false
}