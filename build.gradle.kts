// Top-level build file where you can add configuration options common to all sub-projects/modules.
// Plugins que se usaran en tiempo de compilacion pero no es necesario aplicarla inmediatamente en tiempo de ejecucion
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias (libs.plugins.hilt.plugin) apply false
    alias(libs.plugins.secrets.plugin) apply false
    alias(libs.plugins.google.service) apply false
    alias(libs.plugins.crashlytics) apply false
}

buildscript {
    dependencies {
        classpath(libs.maps.secrets)
    }
}