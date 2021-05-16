import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.5.0"

    // Apply the application plugin to add support for building a CLI application in Java.
    application
}

group = "be.zvz"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
    maven {
        name = "powernukkit-releases"
        url = uri("https://api.bintray.com/maven/powernukkit/powernukkit/PowerNukkit")
    }
    maven {
        name = "powernukkit-snapshots"
        url = uri("https://oss.sonatype.org/content/repositories/snapshots")
    }
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation(group = "org.jetbrains.kotlin", name = "kotlin-stdlib-jdk8")

    // netty
    implementation(group = "io.netty", name = "netty-all", version = "4.1.63.Final")

    // bedrock-network
    implementation(group = "org.powernukkit.bedrock.network", name = "raknet", version = "1.6.25-PN")

    // Use the Kotlin test library.
    testImplementation(group = "org.jetbrains.kotlin", name = "kotlin-test")

    // Use the Kotlin JUnit integration.
    testImplementation(group = "org.jetbrains.kotlin", name = "kotlin-test-junit")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "11"
    }
}

application {
    // Define the main class for the application.
    mainClass.set("be.zvz.kookie.AppKt")
}
