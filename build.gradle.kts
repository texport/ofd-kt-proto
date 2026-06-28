plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.wire) apply false
    alias(libs.plugins.detekt) apply false
}

allprojects {
    repositories {
        mavenLocal()
    mavenCentral()
    }
}
