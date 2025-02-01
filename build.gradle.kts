plugins {
    java
    `java-library`
    `maven-publish`
}

group = "dev.starless"
version = "1.0.2"

repositories {
    mavenCentral()
}

dependencies {
    api("io.lettuce:lettuce-core:6.5.3.RELEASE")
    api("com.google.protobuf:protobuf-java-util:4.29.3")
}

publishing {
    repositories {
        maven {
            name = "milkyway"
            url = uri("https://repo.starless.dev/releases")
            credentials {
                username = properties["milkywayUsername"] as String
                password = properties["milkywayPassword"] as String
            }
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
