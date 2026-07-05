plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.wire) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.nmcp.aggregation)
    alias(libs.plugins.kover)
}

allprojects {
    repositories {
        google()
        mavenLocal()
        mavenCentral()
    }
}

dependencies {
    add("nmcpAggregation", dependencies.project(mapOf("path" to ":ofd-kt-proto")))
}

nmcpAggregation {
    centralPortal {
        username.set(project.findProperty("ossrhUsername")?.toString() ?: System.getenv("OSSRH_USERNAME"))
        password.set(project.findProperty("ossrhPassword")?.toString() ?: System.getenv("OSSRH_PASSWORD"))
        publishingType.set("USER_MANAGED")
    }
}
