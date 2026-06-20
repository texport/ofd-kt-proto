plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.protobuf)
    alias(libs.plugins.detekt)
    `maven-publish`
    signing
}

group = "kz.mybrain"
version = "2.0.3"

dependencies {
    implementation(libs.protobuf.kotlin)
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}

java {
    withSourcesJar()
    withJavadocJar()
}

base {
    archivesName.set("ofd-kt-proto-v203")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            
            pom {
                name.set("ofd-kt-proto-v203")
                description.set("Protobuf generated entities for KazakhTelecom OFD Protocol v2.0.3")
                url.set("https://github.com/texport/ofd-kt-proto")
                
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
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
    
    repositories {
        maven {
            name = "OSSRH"
            val releasesRepoUrl = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            val snapshotsRepoUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
            
            credentials {
                username = project.findProperty("ossrhUsername")?.toString() ?: System.getenv("OSSRH_USERNAME")
                password = project.findProperty("ossrhPassword")?.toString() ?: System.getenv("OSSRH_PASSWORD")
            }
        }
    }
}

signing {
    isRequired = false
    sign(publishing.publications["mavenJava"])
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${libs.versions.protobufJava.get()}"
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                create("kotlin")
            }
        }
    }
}

detekt {
    config.setFrom(files("${rootDir}/config/detekt/detekt.yml"))
    buildUponDefaultConfig = true
    allRules = true
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    exclude("**/build/generated/**")
    exclude("**/generated/source/proto/**")
}
