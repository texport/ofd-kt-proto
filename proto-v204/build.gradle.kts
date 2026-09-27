plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.wire)
    alias(libs.plugins.detekt)
    alias(libs.plugins.kover)
    // Раскладывает собранную библиотеку по схеме Maven-репозитория: из неё
    // выпуск на GitHub собирает архив, который подключают как maven(uri(...)).
    `maven-publish`
}

group = "io.github.texport"
// Версия протокола; номер сборки через дефис (2.0.4-1, 2.0.4-2, …) назначает
// выпуск по меткам git и передаёт свойством -PreleaseVersion. Без него — SNAPSHOT.
val protocol = "2.0.4"
version = providers.gradleProperty("releaseVersion").orNull
    ?.takeIf { it.startsWith("$protocol-") }
    ?: "$protocol-SNAPSHOT"

kotlin {
    jvm()
    android {
        namespace = "kz.mybrain.ofd.proto.v204"
        compileSdk = libs.versions.androidCompileSdk.get().toInt()
        minSdk = libs.versions.androidMinSdk.get().toInt()
        withHostTest {}
    }
    iosArm64()
    iosX64()
    iosSimulatorArm64()

    jvmToolchain(libs.versions.javaTargetCore.get().toInt())


    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.wire.runtime)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }

}

base {
    archivesName.set("ofd-kt-proto-v204")
}

wire {
    sourcePath {
        srcDir("src/main/proto")
    }
    kotlin {
        // Generates KMP compatible classes
    }
}

detekt {
    config.setFrom(files("${rootDir}/config/detekt/detekt.yml"))
    buildUponDefaultConfig = true
    allRules = true
    // Автоправка переписывает исходники на месте; на CI её правки пропадают
    // вместе с раннером, а исправленное нарушение проверку не роняет.
    autoCorrect = System.getenv("CI") == null
    source.setFrom(files("src/commonMain/kotlin", "src/jvmMain/kotlin", "src/androidMain/kotlin", "src/iosMain/kotlin"))
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    exclude("**/build/generated/**")
    exclude("**/generated/source/proto/**")
    exclude("**/generated/source/wire/**")
}

dependencies {
    detektPlugins(libs.detekt.formatting)
}

kover {
    reports {
        verify {
            rule {
                bound {
                    coverageUnits = kotlinx.kover.gradle.plugin.dsl.CoverageUnit.INSTRUCTION
                    minValue = 90
                }
                bound {
                    coverageUnits = kotlinx.kover.gradle.plugin.dsl.CoverageUnit.BRANCH
                    minValue = 94
                }
                bound {
                    coverageUnits = kotlinx.kover.gradle.plugin.dsl.CoverageUnit.LINE
                    minValue = 99
                }
            }
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask<*>>().configureEach {
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
}

tasks.named("koverVerify") {
    enabled = false
}

tasks.named("check") {
    dependsOn("koverVerify")
}
