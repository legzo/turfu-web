import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.50"
    application
}

group = "com.jtelabs"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
}

val http4kVersion = "3.189.0"

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.http4k:http4k-core:$http4kVersion")
    implementation("org.http4k:http4k-server-jetty:$http4kVersion")
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

tasks.register("stage") {
    dependsOn("installDist")
}

application {
    mainClassName = "org.jtelabs.AppKt"
}
