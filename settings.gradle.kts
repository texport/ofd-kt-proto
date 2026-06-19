plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
rootProject.name = "ofd-kt-proto"
include("proto-v203")
project(":proto-v203").name = "ofd-kt-proto-v203"
