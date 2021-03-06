# KIT Data Manager - Service Base Module

![Build Status](https://img.shields.io/travis/kit-data-manager/service-base.svg)
![Code Coverage](https://img.shields.io/coveralls/github/kit-data-manager/service-base.svg)
![License](https://img.shields.io/github/license/kit-data-manager/service-base.svg)

Helper module for KIT DM 2.0 services providing commonly used dependencies and general purpose implementations, e.g. helpers and exception.

## How to build

In order to build this module you'll need:

* Java SE Development Kit 8 or higher

After obtaining the sources change to the folder where the sources are located and call:

```
user@localhost:/home/user/service-base$ ./gradlew install
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
    <version>0.1</version>
</dependency>
~~~~

### Gradle

~~~~
compile group: 'edu.kit.datamanager', name: 'service-base', version: '0.1'
~~~~


## License

The KIT Data Manager is licensed under the Apache License, Version 2.0.