plugins {
    java
    `maven-publish`
}

group = "dev.starless"
version = "1.1.1"

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
    repositories {
        maven {
            name = "milkyway"
            url = uri("https://repo.starless.dev/releases")
            credentials(PasswordCredentials::class)
        }
    }

    publishing {
        publications {
            create<MavenPublication>("maven") {
                from(components["java"])
            }
        }
    }
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }
}
java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))
