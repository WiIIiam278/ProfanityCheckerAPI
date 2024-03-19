# ProfanityCheckerAPI
[![Build Status](https://github.com/WiIIiam278/ProfanityCheckerAPI/actions/workflows/ci.yml/badge.svg)](https://github.com/WiIIiam278/ProfanityCheckerAPI/actions/workflows/ci.yml)
[![Discord](https://img.shields.io/discord/818135932103557162?color=7289da&logo=discord)](https://discord.gg/tVYhJfyDWG)
[![Maven](https://repo.william278.net/api/badge/latest/releases/net/william278/profanitycheckerapi?color=00fb9a&name=Maven&prefix=v)](https://repo.william278.net/#/releases/net/william278/profanitycheckerapi/)


**ProfanityCheckerAPI** is a Java API for checking if text contains profanity via the [alt-profanity-checker](https://pypi.org/project/alt-profanity-check/) Python library. It uses [jep](https://github.com/ninia/jep) to run and interpret Python script to do this.

## Requirements
* Java 11+
* Python 3.8+
* [jep](https://pypi.org/project/jep/) (`pip install jep`)
    * Make sure your library paths are set correctly once you've installed it, as this isn't always the case, particularly on Windows.
    * If you get an "Unsatisfied Link" error, check [here](https://github.com/ninia/jep/wiki/FAQ#how-do-i-fix-unsatisfied-link-error-no-jep-in-javalibrarypath) for how to resolve this.
* [alt-profanity-check](https://pypi.org/project/alt-profanity-check/) (`pip install alt-profanity-check`)

## Installation
ProfanityCheckerAPI is available from [Jitpack.io](https://jitpack.io/#WiIIiam278/ProfanityCheckerAPI). You can also
view the [javadocs](https://javadoc.jitpack.io/net/william278/ProfanityCheckerAPI/latest/javadoc/index.html)
there.

### Maven
To use the library with Maven, first add the jitpack repository to your `pom.xml`
```xml
<repositories>
   <repository>
       <id>jitpack.io</id>
       <url>https://jitpack.io</url>
   </repository>
</repositories>
```
Then, add the dependency in your `<dependencies>` section.
```xml
<dependency>
   <groupId>net.william278</groupId>
   <artifactId>ProfanityCheckerAPI</artifactId>
   <version>2.0.1</version>
   <scope>compile</scope>
</dependency>
```

### Gradle & others
JitPack has [a handy guide](https://jitpack.io/#net.william278/ProfanityCheckerAPI/#How_to) for how to use the dependency with other build platforms.

## Usage
First of all, make sure the environment you're deploying your application on has Python v3.8+ with `jep` and `alt-profanity-check` installed on, and ensure jep's library is in your application's classpath (i.e. by setting LD_LIBRARY_PATH on Linux).

Get a `ProfanityCheckerBuilder` using `ProfanityChecker#builder()`. You can configure the checker to either automatically determine if text is profane (set by default) or specify a threshold double value `withThreshold(double)` to check against. You can additionally specify which normalizers to use. Normalizers adapt the provided string against common bypass techniques, such as leet speak (by default, all normalizers are used). When you've configured the builder, invoke `#build()` to get a ProfanityChecker instance.

With the instance, simply use the `#isProfane(String)` against a string. When you're done with the checker, close it with (`#close`) to dispose of the jep runner instance, or use it in a try-with resources statement.

<details>
  <summary>Code example</summary>
  
  ```java
  // This will return true if the message contains profanity exceeding a threshold of 0.8
  public static boolean isMessageProfane(String message) {
      try (ProfanityChecker checker = ProfanityChecker.builder().withThreshold(0.8d).build()) {
         return checker.isProfane(message);
      }
  }
  ```
  
</details>

* Javadocs for ProfanityCheckerAPI [are available here](https://javadoc.jitpack.io/net/william278/ProfanityCheckerAPI/latest/javadoc/index.html).

## Building
To build ProfanityCheckerAPI, ensure Python v3.8+, `jep` and `alt-profanity-check` (as well as any dependencies) are installed on your machine, then run the following in the root of the repository:
```
./gradlew clean build
```

## License
ProfanityCheckerAPI is licensed under [MIT License](https://github.com/WiIIiam278/ProfanityCheckerAPI/blob/master/LICENSE).

---
&copy; [William278](https://william278.net/), 2022–2024. Licensed under the MIT License.
