import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.50"
}

group = "com.jtelabs"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val http4k_version = "3.189.0"

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.http4k:http4k-core:$http4k_version")
    implementation("org.http4k:http4k-server-jetty:$http4k_version")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("com.github.dpaukov:combinatoricslib3:3.3.0")
    implementation("org.http4k:http4k-core:3.189.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    freeCompilerArgs = listOf("-XXLanguage:+InlineClasses")
}