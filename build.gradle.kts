plugins {
    java
    `maven-publish`
}

group = "dev.starless"
version = "1.1.0"

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
