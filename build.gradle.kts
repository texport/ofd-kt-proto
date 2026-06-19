plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.protobuf) apply false
    alias(libs.plugins.detekt)
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")

    detekt {
        config.setFrom(files("${rootDir}/config/detekt/detekt.yml"))
        buildUponDefaultConfig = true
        allRules = true
    }

    tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
        exclude("**/build/generated/**")
        exclude("**/generated/source/proto/**")
    }
}
