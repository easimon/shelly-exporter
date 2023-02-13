# Changelog

## [2.2.1](https://github.com/easimon/shelly-exporter/compare/2.2.0...2.2.1) (2023-02-13)


### Bug Fixes

* watt-minute meters must have different name ([97abedd](https://github.com/easimon/shelly-exporter/commit/97abedd2df2fcd72e7f8ba3fa4855eab7f77c689))

## [2.2.0](https://github.com/easimon/shelly-exporter/compare/2.1.0...2.2.0) (2023-02-13)


### Features

* expose power consumption in watt-minutes and watt-hours (calculated) ([86025a9](https://github.com/easimon/shelly-exporter/commit/86025a94a69c7d456ebd3ce9ef876b19d9ecf45d))


### Bug Fixes

* migrate deprecated spring boot property ([a001290](https://github.com/easimon/shelly-exporter/commit/a001290ca56ac8978dd2b2dadc1b429058676270))

## [2.1.0](https://github.com/easimon/shelly-exporter/compare/2.0.0...2.1.0) (2023-01-30)


### Features

* add base url redirect ([202d67e](https://github.com/easimon/shelly-exporter/commit/202d67ead6ee75fb59410d80b864640205d660fa))

## [2.0.0](https://github.com/easimon/shelly-exporter/compare/1.1.0...2.0.0) (2022-12-30)


### ⚠ BREAKING CHANGES

* upgrade to spring boot 3

### Features

* upgrade to spring boot 3 ([4118aa2](https://github.com/easimon/shelly-exporter/commit/4118aa2d9c8dd693396f34060affa2ecf9802b4d))

## [1.1.0](https://github.com/easimon/shelly-exporter/compare/1.0.2...1.1.0) (2022-10-16)


### Features

* add meter descriptions. ([1e9b3d6](https://github.com/easimon/shelly-exporter/commit/1e9b3d61c708525658dc56805521959264f57c21))

## [1.0.2](https://github.com/easimon/shelly-exporter/compare/1.0.1...1.0.2) (2022-09-28)


### Bug Fixes

* http client: make scraping errors cacheable as well (by returning null instead of throwing) ([9bdea6b](https://github.com/easimon/shelly-exporter/commit/9bdea6ba0a0a01f316661a1533a9e951a63772cb))
* revert emitting 0 on scraping failure, emit NaN instead. ([ec0044d](https://github.com/easimon/shelly-exporter/commit/ec0044d96ea307f98e98646e2cda0d5bf7db10df))

## [1.0.1](https://github.com/easimon/shelly-exporter/compare/1.0.0...1.0.1) (2022-09-19)


### Bug Fixes

* make scraping error result value configurable, default to 0.0 ([830170d](https://github.com/easimon/shelly-exporter/commit/830170d0ecfea0d9cdc449bfbdcafd786d3e41a9))
* trim device name in registry as well ([c307bd2](https://github.com/easimon/shelly-exporter/commit/c307bd284fd50dac254bed0243728a67c5936317))

## [1.0.0](https://github.com/easimon/shelly-exporter/compare/v1.0.1...1.0.0) (2022-09-11)


### Bug Fixes

* export watt-hours correctly (was watt-minutes) ([7fce810](https://github.com/easimon/shelly-exporter/commit/7fce810b5eac93f57a3ec92caeaf2a89befe523e))
* trigger tagged build, try 2 ([fa855b5](https://github.com/easimon/shelly-exporter/commit/fa855b5b5e1bf8ec357b5576a0433771a0d6fde5))
* trigger tagged docker build on release ([84473f7](https://github.com/easimon/shelly-exporter/commit/84473f734ac87c8c9bf5477c138b87d7ae879fbe))
* trim label values ([0fe560f](https://github.com/easimon/shelly-exporter/commit/0fe560fa3ea69608278e7e9a82f84a9d7dd48ff3))


### Miscellaneous Chores

* release 1.0.0 ([4e421f0](https://github.com/easimon/shelly-exporter/commit/4e421f0f2dfd322bf1a649a0cdc3d842238f4b45))

## Changelog
