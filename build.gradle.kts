plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.wire) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.kover)
}

allprojects {
    repositories {
        google()
        mavenLocal()
        mavenCentral()
    }
}
