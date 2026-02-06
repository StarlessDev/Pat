plugins {
    id("java-library")
    id("maven-publish")
}

group = "com.fabiodm.pat"
version = "1.1.3"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(libs.lettuce)

    // Optional dependencies used for parsers
    compileOnly(libs.protobuf)
    compileOnly(libs.gson)
}

tasks.compileJava {
    options.encoding = Charsets.UTF_8.name()
    options.release.set(17) // Support from Java 17 to Java 25
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }

    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifactId = tasks.jar.get().archiveBaseName.get()
        }
    }
}
