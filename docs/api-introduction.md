## Developer API Introduction

### Adding faces to your project
The first step to using `creative-faces` is to add it to your project, you can do
this using Gradle *(recommended)* or Maven

#### Gradle
If you are using Gradle, you have to add our repository and the faces dependency
to your build script *(`build.gradle(.kts)`)*

##### Kotlin DSL:
```kotlin
repositories {
    maven("https://repo.unnamed.team/repository/unnamed-public/")
}

dependencies {
    compileOnly("team.unnamed:creative-faces:%%REPLACE_latestReleaseOrSnapshot{team.unnamed:creative-faces}%%")
}
```

##### Groovy DSL:
```groovy
repositories {
    maven {
        url "https://repo.unnamed.team/repository/unnamed-public/"
    }
}

dependencies {
    compileOnly "team.unnamed:creative-faces:%%REPLACE_latestReleaseOrSnapshot{team.unnamed:creative-faces}%%"
}
```

#### Maven
If you are using Maven, you have to add our repository and the faces dependency
to your `pom.xml`
```xml
<repositories>
    <repository>
        <id>unnamed-public</id>
        <url>https://repo.unnamed.team/repository/unnamed-public/</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>team.unnamed</groupId>
        <artifactId>creative-faces</artifactId>
        <version>%%REPLACE_latestReleaseOrSnapshot{team.unnamed:creative-faces}%%</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

### Declaring a dependency on faces

If you are developing a plugin that uses `creative-faces`, you should declare a dependency
on it in your plugin's `plugin.yml`, this will make sure that your plugin will not be loaded
if `creative-faces` is not present

**plugin.yml in Bukkit plugins**
```yaml
name: ...
main: ...
version: ...
...
depend: [creative-faces]
```

### Accessing the API

First of all, you need to obtain an instance of `FaceProvider`, you can do this by using
your platform's service provider

**Using Bukkit's service provider**
```java
RegisteredServiceProvider<FaceProvider> provider = Bukkit.getServicesManager().getRegistration(FaceProvider.class);
if (provider != null) {
    FaceProvider faceProvider = provider.getProvider();
    // ...
}
```

Now that we have a `FaceProvider` instance, we can get to the next step