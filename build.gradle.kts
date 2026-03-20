plugins {
    id("java-library")
    id("maven-publish")
}

group = "dev.starless"
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

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17

    withJavadocJar()
    withSourcesJar()
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }
}
