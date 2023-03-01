val ktor_version: String by project

plugins {
    kotlin("jvm") version "1.8.0"
    id("io.ktor.plugin") version "2.2.3"
    application
}

group = "io.arcoacademy"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.12.5")

    implementation("io.ktor:ktor-serialization-jackson-jvm:$ktor_version")

    implementation("software.amazon.awssdk:sqs:2.17.90")

    implementation("com.github.javafaker:javafaker:1.0.2")

    implementation("org.slf4j:slf4j-log4j12:2.0.6")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("MainKt")
}