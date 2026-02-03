plugins {
    id("java-library")
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
    options.release.set(25)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}
