# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added

### Fixed

### Security

## [1.3.5] - 2025-08-08

### Security
* Updated springDoc to version 2.8.9
* Updated several core dependencies and plugins
* Updated gradle to 9.0.0

## [1.3.4] - 2025-05-02

### Added
* Filter to remove duplicated headers in the response of the Elastic proxy endpoint.
* Enhance search configuration to allow setting endpoint for Elastic proxy endpoint and the headers which should be removed from the response.

### Security
* Updated several core dependencies and plugins
* Updated gradle to 8.14

## [1.3.3] - 2025-01-16

### Security
* Updated the version of the `nimbus-jose-jwt` library from to 10.0.1 for improved functionality and security.
* Updated the Gradle wrapper to version 8.12 for enhanced performance and compatibility.
* Updated the version of the `commons-io` library from 2.17.0 to 2.18.0 for improved performance.
* Updated Spring Boot to version 3.4.1 for new features and improvements.
* Updated several Jackson library versions for better performance and functionality.

## [1.3.2] - 2024-11-08

### Added
* Added a Javadoc badge to the README for easier access to documentation.

### Fixed
* Enhanced error handling and portability in the gradlew and gradlew.bat scripts.

### Security
* Updated Java setup action versions across various workflow files for improved compatibility.
* Upgraded multiple plugin and dependency versions in the build configuration for better performance and security.
* Updated Gradle distribution version and added new properties for improved configuration.

## [1.3.1] - 2024-04-17

### Fixed
* Problem while validating keycloak tokens.

## [1.3.0] - 2024-04-12

### Added
* Additional Elastic proxy endpoint added which reflects the correct Elastic API, i.e., is available via /api/v1/{index}/_search
* ADMINISTRATOR role is now properly evaluated in search requests by @github-actions in https://github.com/kit-data-manager/service-base/pull/254

### Security
* Bump commons-io:commons-io from 2.16.0 to 2.16.1 
* Bump io.github.gradle-nexus.publish-plugin from 2.0.0-rc-2 to 2.0.0 

## [1.2.1] - 2024-04-02

### Changed
* SimpleServiceClient now follows redirects (HTTP 3XX) properly
* Enable redirections for getResource and getResources for SimpleServiceClient by @VolkerHartmann in https://github.com/kit-data-manager/service-base/pull/227

### Removed
* Removed unused (and broken) methods 'getPages' and 'getPage' from internal class ContentRange 

### Fixed
* Fixed JWT handling caused by API changes in dependencies
* Fixed development branch build on push. by @github-actions in https://github.com/kit-data-manager/service-base/pull/225
* Fixed Content-Range header according to RFT 7233 by @github-actions in https://github.com/kit-data-manager/service-base/pull/241

### Security
* Bump actions/checkout to 4
* Bump github/codeql-action to 3
* Bump codecov/codecov-action to 4
* Bump actions/setup-java to 4.2.1
* Bump publish-plugin to 2.0.0-rc2
* Bump jacoco to 0.8.11
* Bump io.freefair.lombok to 8.6
* Bump io.freefair.maven-publish-java to 8.6
* Bump org.owasp.dependencycheck to 9.1.0
* Bump io.spring.dependency-management to 1.1.4
* Bump springDocVersion to 1.8.0
* Bump javassist to 3.30.2-GA
* Bump commons-validator to 1.8.0
* Bump commons-lang3 to 3.14.0
* Bump commons-io:commons-io 2.16.0
* Bump jackson-jaxrs-json-provider to 2.17.0
* Bump jackson-module-afterburner to 2.17.0
* Bump jackson-datatype-joda to 2.17.0
* Bump jackson-datatype-jsr310 to 2.17.0
* Bump jackson-databind to 2.16.2
* Bump datatype:jackson-datatype-jsr310 to 2.17.0
* Bump jaxb-impl to 4.0.5
* Bump jaxb-core to 4.0.5
* Bump tika-core to 2.9.1
* Bump nimbus-jose-jwt to 9.37.3
* Bump jjwt-api to 0.12.5
* Bump jjwt-impl to 0.12.5
* Bump jjwt-jackson to 0.12.5
* Bump dozer-core to 7.0.0

## [1.2.0] - 2023-06-19

### Added
- Add credentials for unauthenticated users.

### Changed
- At least JDK 17 is now required.
- Bump some github actions from 2 to 3.
- Bump com.fasterxml.jackson.datatype:jackson-datatype-joda from 2.14.1 to 2.15.2.
- Bump com.fasterxml.jackson.datatype:jackson-datatype-jsr310 from 2.14.1 to 2.15.2.
- Bump com.fasterxml.jackson.jaxrs:jackson-jaxrs-json-provider from 2.14.1 to 2.15.2.
- Bump com.fasterxml.jackson.module:jackson-module-afterburner from 2.14.1 to 2.15.2.
- Bump com.sun.xml.bind:jaxb-core from 4.0.1 to 4.0.3.
- Bump com.sun.xml.bind:jaxb-impl from 4.0.1 to 4.0.3. 
- Bump commons-io:commons-io from 2.11.0 to 2.13.0. 
- Bump io.freefair.lombok from 6.6.1 to 8.0.1.
- Bump io.freefair.maven-publish-java from 6.6.1 to 8.0.1.
- Bump io.github.gradle-nexus.publish-plugin from 1.1.0 to 1.3.0.
- Bump JAVA version to 17 due to Spring Boot 3. 
- Bump org.mockito:mockito-inline from 4.11.0 to 5.2.0.
- Bump org.owasp.dependencycheck from 7.4.4 to 8.2.1.
- Bump org.springframework.boot:spring-boot-dependencies from 2.7.7 to 3.1.0.
- Bump springDocVersion from 1.6.14 to 1.7.0.

### Fixed
- Fix misleading log for elasticsearch.

## [1.1.1] - 2023-03-17

### Added
- Support group memberships from JWT for authorization decisions by @ThomasJejkal in https://github.com/kit-data-manager/service-base/pull/124

### Changed
- Elastic search configuration validation now retries three times with 5 seconds delay to connect to repo.search.url before startup fails in case of slow Elastic startup
- Improve documentation on SearchConfiguration usage by @ThomasJejkal in https://github.com/kit-data-manager/service-base/pull/122
- Bump mockito-inline from 4.10.0 to 4.11.0 by @dependabot in https://github.com/kit-data-manager/service-base/pull/120
- Bump httpclient from 4.5.13 to 4.5.14 by @dependabot in https://github.com/kit-data-manager/service-base/pull/119
- Bump nimbus-jose-jwt from 9.27 to 9.28 by @dependabot in https://github.com/kit-data-manager/service-base/pull/118

### Deprecated
- set/getGroupId in JwtAuthenticationToken is deprecated and will be removed in the next major version. Instead, set/getGroups should be used.

## [1.1.0] - 2023-01-11

### Added
- Search endpoint (proxy) for elasticsearch

### Changed
- Update to gradle version 7.6
- Bump spring-boot-dependencies from 2.7.4 to 2.7.7
- Bump org.owasp.dependencycheck from 7.4.1 to 7.4.4 
- Bump io.freefair.maven-publish-java from 6.5.1 to 6.6.1
- Bump io.freefair.lombok from 6.5.1 to 6.6.1 
- Bump nimbus-jose-jwt from 9.25.6 to 9.27
- Bump springDocVersion from 1.6.12 to 1.6.14 
- Bump mockito-inline from 4.8.1 to 4.10.0
- Bump jackson-datatype-joda from 2.13.4 to 2.14.1 
- Bump jackson-module-afterburner from 2.13.4 to 2.14.1 
- Bump jackson-datatype-jsr310 from 2.13.4 to 2.14.1 
- Bump jackson-jaxrs-json-provider from 2.13.4 to 2.14.1

### Removed
- Remove log libraries: use standard libraries from Spring Boot

### Deprecated

### Fixed
- User role INACTIVE is now properly checked. If a user is marked inactive, all access attempts will be answered with HTTP 403 (FORBIDDEN)

## [1.0.7] - 2022-10-14

### Fixed

- Finally fixed typecast problem for array claims in JWT handling

## [1.0.6] - 2022-10-14

### Fixed

- Fixed typecast problem for array claims in JWT handling

## [1.0.5] - 2022-10-13
### Added

### Changed

- Update to io.freefair.lombok 6.5.1
- Update to io.freefair.maven-publish-java 6.5.1
- Update to io.spring.dependency-management 1.0.14.RELEASE
- Update to org.owasp.dependencycheck 7.2.1
- Update to com.jfrog.bintray 1.8.5
- Update to net.researchgate.release 3.0.2
- Update to spring-boot 2.7.4
- Update to spring-doc 1.6.11
- Update tojackson-jaxrs-json-provider 2.13.4
- Update to jackson-datatype-joda 2.13.4
- Update jackson-datatype-jsr310 2.13.4
- Update to com.nimbusds:nimbus-jose-jwt 9.25.4
- Update to io.jsonwebtoken:jjwt-api 0.11.5
- Update to io.jsonwebtoken:jjwt-impl 0.11.5
- Update to io.jsonwebtoken:jjwt-jackson 0.11.5
- Update to org.slf4j:slf4j-api 1.7.36
- Update to com.sun.xml.bind:jaxb-core 4.0.1
- Update to com.sun.xml.bind:jaxb-impl 4.0.1
- Update to org.javassist:javassist 3.29.2-GA

### Removed

### Deprecated

### Fixed

## [1.0.4] - 2022-07-30
### Added

- RabbitMQ-based messaging credentials are now configurable in application.properties via properties repo.messaging.username and repo.messaging.password

### Changed

- Update to net.researchgate.release 3.0.0
- Update to spring-boot 2.7.2
- Update to spring-doc 1.6.9

## [1.0.3] - 2022-06-13
### Fixed
- Request handling is now properly stopped if expired/invalid JWT was provided

### Security

## [1.0.2] - 2022-06-03
### Changed
- Anonymous user is now added to the authorities by default. 

### Fixed
- Request handling is now properly stopped if expired/invalid JWT was provided

## [1.0.1] - 2022-03-23
### Changed
- RabbitMQ no longer started by default.

## [1.0.0] - 2022-03-09
### Added
 - Keycloak support added to JWT security filters

### Changed
- Update to jjwt 0.11.2

## [0.3.2] - 2021-12-13
### Changed
 - Update to Spring-Boot 2.4.13
 - Update to dozer-core 6.5.2
 - Update to json-patch 1.13
 - Update to jackson-jaxrs-json-provider 2.13.0
 - Update to jackson-module-afterburner 2.13.0
 - Update to jackson-datatype-jsr310 2.13.0
 - Update to jackson-datatype-joda 2.13.0

## [0.3.1] - 2021-11-30
### Fixed
- Support for additional date patterns (yyyy, yyyy-MM, and yyyy-MM-dd) in CustomInstantDeserializer

### Changed
- Removed (unused) coveralls and grgit plugins to sustain JDK8 compatibility

## [0.3.0] - 2021-10-13
### Added
- Add GitHub actions
### Changed
- Upgrade to Spring Boot 2.4.10
- Upgrade Gradle to 7.2

## [0.2.1] - 2021-01-14
### Fixed
- Removed @Component annotation in edu.kit.datamanager.dao.ByExampleSpecification to allow using service-base without database

## [0.2.0] - 2020-12-15
### Changed
- Renaming of RabbitMQ configuration property repo.messaging.exchange to repo.messaging.sender.exchange

## [0.1.3] - 2020-11-25
### Changed
- Truncating service-assigned times to milliseconds for compatibility reasons

### Fixed
- Fix of wrong HATEOS links in pagination

## [0.1.2] - 2020-09-28
### Changed
- Minor changes in messaging classes

## [0.1.1] - 2020-08-18
### Fixed
- Fix of wrong HATEOS links in pagination

## [0.1] - 2020-06-30
### Added
- First public version

### Changed
- none

### Removed
- none

### Deprecated
- none

### Fixed
- none

### Security
- none

[Unreleased]: https://github.com/kit-data-manager/service-base/compare/v1.3.4...HEAD
[1.3.4]: https://github.com/kit-data-manager/service-base/compare/v1.3.3...v1.3.4
[1.3.3]: https://github.com/kit-data-manager/service-base/compare/v1.3.2...v1.3.3
[1.3.2]: https://github.com/kit-data-manager/service-base/compare/v1.3.1...v1.3.2
[1.3.1]: https://github.com/kit-data-manager/service-base/compare/v1.3.0...v1.3.1
[1.3.0]: https://github.com/kit-data-manager/service-base/compare/v1.2.1...v1.3.0
[1.2.1]: https://github.com/kit-data-manager/service-base/compare/v1.2.0...v1.2.1
[1.2.0]: https://github.com/kit-data-manager/service-base/compare/v1.1.1...v1.2.0
[1.1.1]: https://github.com/kit-data-manager/service-base/compare/v1.1.0...v1.1.1
[1.1.0]: https://github.com/kit-data-manager/service-base/compare/v1.0.7...v1.1.0
[1.0.7]: https://github.com/kit-data-manager/service-base/compare/v1.0.6...v1.0.7
[1.0.6]: https://github.com/kit-data-manager/service-base/compare/v1.0.5...v1.0.6
[1.0.5]: https://github.com/kit-data-manager/service-base/compare/v1.0.4...v1.0.5
[1.0.4]: https://github.com/kit-data-manager/service-base/compare/v1.0.3...v1.0.4
[1.0.3]: https://github.com/kit-data-manager/service-base/compare/v1.0.2...v1.0.3
[1.0.2]: https://github.com/kit-data-manager/service-base/compare/v1.0.1...v1.0.2
[1.0.1]: https://github.com/kit-data-manager/service-base/compare/v1.0.0...v1.0.1
[1.0.0]: https://github.com/kit-data-manager/service-base/compare/v0.3.2...v1.0.0
[0.3.2]: https://github.com/kit-data-manager/service-base/compare/v0.3.1...v0.3.2
[0.3.1]: https://github.com/kit-data-manager/service-base/compare/v0.3.0...v0.3.1
[0.3.0]: https://github.com/kit-data-manager/service-base/compare/v0.2.1...v0.3.0
[0.2.1]: https://github.com/kit-data-manager/service-base/compare/v0.2.0...v0.2.1
[0.2.0]: https://github.com/kit-data-manager/service-base/compare/0.1.3...v0.2.0
[0.1.3]: https://github.com/kit-data-manager/service-base/compare/0.1.2...0.1.3
[0.1.2]: https://github.com/kit-data-manager/service-base/compare/0.1.1...0.1.2
[0.1.1]: https://github.com/kit-data-manager/service-base/compare/0.1...0.1.1
[0.1]: https://github.com/kit-data-manager/service-base/releases/tag/0.1
