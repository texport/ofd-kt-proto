plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.wire)
    alias(libs.plugins.detekt)
    alias(libs.plugins.kover)
    `maven-publish`
    signing
}

group = "io.github.texport"
version = "2.0.3-2"

kotlin {
    jvm()
    android {
        namespace = "kz.mybrain.ofd.proto.v203"
        compileSdk = libs.versions.androidCompileSdk.get().toInt()
        minSdk = libs.versions.androidMinSdk.get().toInt()
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
    archivesName.set("ofd-kt-proto")
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
    autoCorrect = true
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

publishing {
    publications.withType<MavenPublication>().configureEach {
        val javadocJarTask = tasks.register<Jar>("${name}JavadocJar") {
            description = "Generates Javadoc jar for publication ${this@configureEach.name}"
            archiveClassifier.set("javadoc")
            archiveAppendix.set(this@configureEach.name)
        }
        artifact(javadocJarTask)
        pom {
            name.set("ofd-kt-proto")
            description.set("Kotlin Multiplatform Protobuf definitions for KazakhTelecom OFD communication")
            url.set("https://github.com/texport/ofd-kt-proto")
            
            licenses {
                license {
                    name.set("The Apache License, Version 2.0")
                    url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                }
            }
            
            developers {
                developer {
                    id.set("texport")
                    name.set("Sergey Ivanov")
                    email.set("ivanov.sergey.ekb@gmail.com")
                }
            }
            
            scm {
                connection.set("scm:git:git://github.com/texport/ofd-kt-proto.git")
                developerConnection.set("scm:git:ssh://github.com:texport/ofd-kt-proto.git")
                url.set("https://github.com/texport/ofd-kt-proto")
            }
        }
    }
}

signing {
    isRequired = false
    sign(publishing.publications)
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
