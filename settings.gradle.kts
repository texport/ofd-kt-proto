plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "ofd-kt-proto"
include("proto-v203")
project(":proto-v203").name = "ofd-kt-proto-v203"
