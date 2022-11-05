# ProfanityCheckerAPI
[![GitHub CI](https://img.shields.io/github/workflow/status/WiIIiam278/ProfanityCheckerAPI/Java%20CI?logo=github)](https://github.com/WiIIiam278/ProfanityCheckerAPI/actions/workflows/java_ci.yml)
[![JitPack API](https://img.shields.io/jitpack/version/net.william278/ProfanityCheckerAPI?color=%2300fb9a&label=api&logo=gradle)](https://jitpack.io/#net.william278/ProfanityCheckerAPI)
[![Discord](https://img.shields.io/discord/818135932103557162.svg?label=&logo=discord&logoColor=fff&color=7389D8&labelColor=6A7EC2)](https://discord.gg/tVYhJfyDWG)

A Java API for checking if text contains profanity via the [alt-profanity-checker](https://pypi.org/project/alt-profanity-check/) Python library. It uses [jep](https://github.com/ninia/jep) to run and interpret Python script to do this.

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
   <version>1.2</version>
   <scope>compile</scope>
</dependency>
```

### Gradle & others
JitPack has [a handy guide](https://jitpack.io/#net.william278/ProfanityCheckerAPI/#How_to) for how to use the dependency with other build platforms.

## Usage
Create an instance of the `ProfanityChecker` class to get started. It is recommended that you handle these operations on a separate thread from the main thread as jep can be an expensive blocking operation.

You can then simply get if text is profane using the `#isTextProfane(String)` method. Alternatively, you can return a probability double (0 to 1 inclusive) of how likely the machine learning algorithm thinks the text contains profanity using the `#getTextProfanityLikelihood(String)` method.

You should not make more than one instance of `ProfanityChecker` on the same thread, as this can result in an exception from jep. Once you are done with a ProfanityChecker, you can safely dispose of it using the `#dispose` method.

Javadocs for ProfanityCheckerAPI [are available here](https://javadoc.jitpack.io/net/william278/ProfanityCheckerAPI/latest/javadoc/index.html).

## Building
To build ProfanityCheckerAPI, ensure Python v3.8+, jep and alt-profanity-check (as well as any dependencies) are installed on your machine, then run the following in the root of the repository:
```
./gradlew clean build
```

## License
ProfanityCheckerAPI is licensed under [Apache-2.0 License](https://github.com/WiIIiam278/ProfanityCheckerAPI/blob/master/LICENSE).

---
&copy; [William278](https://william278.net/), 2022. Licensed under the Apache-2.0 License.