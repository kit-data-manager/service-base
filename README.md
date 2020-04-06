# KIT Data Manager - Service Base Module

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

## License

The KIT Data Manager is licensed under the Apache License, Version 2.0.