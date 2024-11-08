# KIT Data Manager - Service Base Module

[![build with gradle](https://github.com/kit-data-manager/service-base/actions/workflows/gradle.yml/badge.svg)](https://github.com/kit-data-manager/service-base/actions/workflows/gradle.yml)
[![javadoc](https://javadoc.io/badge2/edu.kit.datamanager/service-base/javadoc.svg)](https://javadoc.io/doc/edu.kit.datamanager/service-base)
[![codecov](https://codecov.io/gh/kit-data-manager/service-base/branch/master/graph/badge.svg)](https://codecov.io/gh/kit-data-manager/service-base)
[![CodeQL](https://github.com/kit-data-manager/service-base/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/kit-data-manager/service-base/actions/workflows/codeql-analysis.yml)
![License](https://img.shields.io/github/license/kit-data-manager/service-base.svg)

Helper module for KIT DM 2.0 services providing commonly used dependencies and general purpose implementations, e.g. helpers and exception.

## How to build

In order to build this module you'll need:

* Java SE Development Kit 17 or higher

After obtaining the sources change to the folder where the sources are located and call:

```
user@localhost:/home/user/service-base$ ./gradlew publishToMavenLocal
BUILD SUCCESSFUL in 1s
3 actionable tasks: 3 executed
user@localhost:/home/user/service-base$
```

The gradle wrapper will download and install gradle, if not already available. Afterwards, the module artifact
will be built and installed into the local maven repository, from where it can be used by other projects.

## Dependency from Maven Central Repository

Instead of using a local build you may also use the most recent version from the Central Maven Repository directly. 

### Maven

~~~~
<dependency>
    <groupId>edu.kit.datamanager</groupId>
    <artifactId>service-base</artifactId>
    <version>1.1.0</version>
</dependency>
~~~~

### Gradle (short)

~~~~
    implementation "edu.kit.datamanager:service-base:1.1.0"
~~~~


## License

The KIT Data Manager is licensed under the Apache License, Version 2.0.
