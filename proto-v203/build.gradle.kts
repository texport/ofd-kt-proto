plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.wire)
    alias(libs.plugins.detekt)
    alias(libs.plugins.nmcp)
    `maven-publish`
    signing
}

group = "io.github.texport"
version = "2.0.4"

kotlin {
    jvm()
    iosArm64()
    iosX64()
    iosSimulatorArm64()

    jvmToolchain(libs.versions.java.get().toInt())

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

    targets.all {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    freeCompilerArgs.add("-Xexpect-actual-classes")
                }
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
    source.setFrom(files("src/commonMain/kotlin", "src/jvmMain/kotlin", "src/iosMain/kotlin"))
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
        val javadocJarTask = tasks.register<org.gradle.api.tasks.bundling.Jar>("${name}JavadocJar") {
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

nmcp {
    publishAllPublicationsToCentralPortal {
        username.set(project.findProperty("ossrhUsername")?.toString() ?: System.getenv("OSSRH_USERNAME"))
        password.set(project.findProperty("ossrhPassword")?.toString() ?: System.getenv("OSSRH_PASSWORD"))
        publishingType.set("USER_MANAGED")
    }
}
