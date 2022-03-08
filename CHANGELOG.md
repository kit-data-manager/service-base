# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

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

[Unreleased]: https://github.com/kit-data-manager/service-base/compare/v0.3.1...HEAD
[0.3.2]: https://github.com/kit-data-manager/service-base/compare/v0.3.1...v0.3.2
[0.3.1]: https://github.com/kit-data-manager/service-base/compare/v0.3.0...v0.3.1
[0.3.0]: https://github.com/kit-data-manager/service-base/compare/v0.2.1...v0.3.0
[0.2.1]: https://github.com/kit-data-manager/service-base/compare/v0.2.0...v0.2.1
[0.2.0]: https://github.com/kit-data-manager/service-base/compare/v0.1.3...v0.2.0
[0.1.3]: https://github.com/kit-data-manager/service-base/compare/v0.1.2...v0.1.3
[0.1.2]: https://github.com/kit-data-manager/service-base/compare/v0.1.1...v0.1.2
[0.1.1]: https://github.com/kit-data-manager/service-base/compare/v0.1.0...v0.1.1
[0.1]: https://github.com/kit-data-manager/service-base/releases/tag/v0.1
