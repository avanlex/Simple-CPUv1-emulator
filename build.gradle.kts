import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.10"
    application
}
group = "me.aleksey"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
dependencies {
    testImplementation(kotlin("test-junit5"))
    testImplementation("io.kotest:kotest-runner-junit5:<version>") // for kotest framework
    testImplementation("io.kotest:kotest-assertions-core:<version>") // for kotest core jvm assertions
    testImplementation("io.kotest:kotest-property:<version>") // for kotest property test
}
tasks.withType<Test> {
    useJUnitPlatform()
}
tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}
application {
    mainClassName = "MainKt"
}