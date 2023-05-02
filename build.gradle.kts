plugins {
    java
}

group = "team.unnamed"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.unnamed.team/repository/unnamed-public/") // Creative Central
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/") // PlaceholderAPI
    mavenLocal()
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")

    // Creative Central
    compileOnly("team.unnamed:creative-central-api:0.7.0-SNAPSHOT")

    // Plugins we hook
    compileOnly("io.github.miniplaceholders:miniplaceholders-api:2.1.0")
    compileOnly("me.clip:placeholderapi:2.11.3")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}