# ProfanityCheckerAPI

[![](https://jitpack.io/v/WiIIiam278/ProfanityCheckerAPI.svg)](https://jitpack.io/#WiIIiam278/ProfanityCheckerAPI)

A Java API for checking if text contains profanity via
the [alt-profanity-checker](https://pypi.org/project/alt-profanity-check/) Python library.

It uses [jep](https://github.com/ninia/jep) to run and interpret Python script to do this.

## Requirements

* Java 11+
* Python 3.8+
* [jep](https://pypi.org/project/jep/) (`pip install jep`)
    * Make sure your library paths are set correctly once you've installed it, as this isn't always the case,
      particularly on Windows.
    * If you get an "Unsatisfied Link" error,
      check [here](https://github.com/ninia/jep/wiki/FAQ#how-do-i-fix-unsatisfied-link-error-no-jep-in-javalibrarypath)
      for solutions.
* [alt-profanity-check](https://pypi.org/project/alt-profanity-check/) (`pip install alt-profanity-check`)

## Maven

ProfanityCheckerAPI is available from [Jitpack.io](https://jitpack.io/#WiIIiam278/ProfanityCheckerAPI). You can also
view the [javadocs](https://javadoc.jitpack.io/com/github/WiIIiam278/ProfanityCheckerAPI/latest/javadoc/index.html)
there.

### Add the repository

```xml
<repositories>
   <repository>
       <id>jitpack.io</id>
       <url>https://jitpack.io</url>
   </repository>
</repositories>
```

### Add the dependency

```xml
<dependency>
   <groupId>com.github.WiIIiam278</groupId>
   <artifactId>ProfanityCheckerAPI</artifactId>
   <version>1.1</version>
   <scope>compile</scope>
</dependency>
```

## Usage

Create an instance of the `ProfanityChecker` class to get started. It is recommended that you handle these operations on
a separate thread from the main thread as jep can be an expensive blocking operation.

You can then simply get if text is profane using the `#isTextProfane(String)` method. Alternatively, you can return a
probability double (0 to 1 inclusive) of how likely the machine learning algorithm thinks the text contains profanity
using the `#getTextProfanityLikelihood(String)` method.

You should not make more than one instance of `ProfanityChecker` on the same thread, as this can result in an exception
from jep. Once you are done with a ProfanityChecker, you can safely dispose of it using the `#dispose` method.

Javadocs for
ProfanityCheckerAPI [are available here](https://javadoc.jitpack.io/com/github/WiIIiam278/ProfanityCheckerAPI/latest/javadoc/index.html).
