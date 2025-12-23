plugins {
    kotlin("jvm") version "2.2.0"
    id("com.google.protobuf") version "0.9.4"
}

group = "org.epicsquad.kkm"
version = "2.0.3"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.protobuf:protobuf-kotlin:4.27.0")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}

base {
    archivesName.set("ofd-kt-proto")
}


protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:4.27.0"
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                create("kotlin")
            }
        }
    }
}
