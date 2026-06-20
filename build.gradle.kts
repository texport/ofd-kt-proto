plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.protobuf) apply false
    alias(libs.plugins.detekt) apply false
}

allprojects {
    repositories {
        mavenLocal()
    mavenCentral()
    }
}
