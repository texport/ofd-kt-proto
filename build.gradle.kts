plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.wire) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.nmcp) apply false
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

// Версии модулей объявлены в их собственных сборках, поэтому корень читает
// их после того, как сборки модулей выполнены.
evaluationDependsOnChildren()

// Протоколы 2.0.3 и 2.0.4 выпускаются порознь, каждый своей версией. Выгрузка
// в Central версии, которая там уже есть, роняет весь выпуск, поэтому в него
// попадает только модуль с версией -PreleaseVersion. Без свойства — все модули.
val releaseVersion = providers.gradleProperty("releaseVersion").orNull

dependencies {
    subprojects
        .filter { releaseVersion == null || it.version.toString() == releaseVersion }
        .forEach { add("nmcpAggregation", project(it.path)) }
}

nmcpAggregation {
    centralPortal {
        username.set(project.findProperty("ossrhUsername")?.toString() ?: System.getenv("OSSRH_USERNAME"))
        password.set(project.findProperty("ossrhPassword")?.toString() ?: System.getenv("OSSRH_PASSWORD"))
        publishingType.set("AUTOMATIC")
    }
}
