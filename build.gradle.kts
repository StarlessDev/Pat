plugins {
    java
    `java-library`
    `maven-publish`
}

group = "dev.starless"
version = "1.0.5"

repositories {
    mavenCentral()
}

dependencies {
    api(libs.lettuce)
    compileOnly(libs.protobuf)
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
