# Changelog

## [2.8.0](https://github.com/easimon/shelly-exporter/compare/2.7.1...2.8.0) (2025-07-28)


### Features

* Add support for Shelly Pro EM 50 ([f170bb0](https://github.com/easimon/shelly-exporter/commit/f170bb0e10bb2b6cf867f13285f4c8fc6d71eb9b))

## [2.7.1](https://github.com/easimon/shelly-exporter/compare/2.7.0...2.7.1) (2025-06-24)


### Bug Fixes

* support null values in the name property of Gen2ShellyDeviceInfo ([79d3790](https://github.com/easimon/shelly-exporter/commit/79d37903f88bed5ab7003e6182f79d727c77dd23))
* support null values in the name property of Gen2ShellyDeviceInfo ([26e641f](https://github.com/easimon/shelly-exporter/commit/26e641f78ad5cd09c15fcdc6feb578a1a334887c))

## [2.7.0](https://github.com/easimon/shelly-exporter/compare/2.6.1...2.7.0) (2025-01-28)


### Features

* Upgrade to OpenJDK 21 ([5e62fa4](https://github.com/easimon/shelly-exporter/commit/5e62fa4c74b7382b718d9fa0f203707526d15148))


### Bug Fixes

* add caffeine stats, mainly to suppress warning ([ed8cc2d](https://github.com/easimon/shelly-exporter/commit/ed8cc2d600a1ea5a241b9409c31ddad3536eb7a9))
* total power consumption and production must be counters ([d9c8aa8](https://github.com/easimon/shelly-exporter/commit/d9c8aa8c842de8f29058ea186862e1aec92a38da))

## [2.6.1](https://github.com/easimon/shelly-exporter/compare/2.6.0...2.6.1) (2025-01-20)


### Bug Fixes

* refactor code to mitigate invalid smart cast ([#125](https://github.com/easimon/shelly-exporter/issues/125)) ([a5c78a4](https://github.com/easimon/shelly-exporter/commit/a5c78a4308ca04caa616fd08c793f7c5a05153e8))
* temperature metrics always need a channel ([0627d44](https://github.com/easimon/shelly-exporter/commit/0627d4454970e2166bd9ad70c6048340d707b529))

## [2.6.0](https://github.com/easimon/shelly-exporter/compare/2.5.9...2.6.0) (2024-12-20)


### Features

* Gen2: Add support for switches and inputs ([fa6715a](https://github.com/easimon/shelly-exporter/commit/fa6715a677c4fe6004f129fc14c6f2736c41aa88))


### Bug Fixes

* Gen2: make all status fields optional ([6123933](https://github.com/easimon/shelly-exporter/commit/6123933831ff511df38fd92ec399e1b37c1671ac))

## [2.5.9](https://github.com/easimon/shelly-exporter/compare/2.5.8...2.5.9) (2024-08-05)


### Bug Fixes

* do not require the (unused) 'auth_domain' field in gen 2 deviceinfo ([8330e46](https://github.com/easimon/shelly-exporter/commit/8330e46aaea5dee5e0807d6f190b84e5abaaca5d))

## [2.5.8](https://github.com/easimon/shelly-exporter/compare/2.5.7...2.5.8) (2024-05-30)

### Bug Fixes

* log exceptions to debug
  only ([9541d28](https://github.com/easimon/shelly-exporter/commit/9541d2892191b2136c74f0f5ac7e81ee5f557ded))
* update invalid metrics filter for prometheus-metrics
  1.2.1 ([6bf93e9](https://github.com/easimon/shelly-exporter/commit/6bf93e9375fee356d87615f7209722c588729960))

## [2.5.7](https://github.com/easimon/shelly-exporter/compare/2.5.6...2.5.7) (2024-05-24)

### Bug Fixes

* make some metrics attributes optional in Gen1 devices for
  real ([f9eb7df](https://github.com/easimon/shelly-exporter/commit/f9eb7df1c513f00c3a2b74922bae7a349f5ee922))

## [2.5.6](https://github.com/easimon/shelly-exporter/compare/2.5.5...2.5.6) (2024-05-22)

### Dependencies

- Spring Boot 3.2.5
- Kotlin 2.0.0
- Kotest 5.9.0

### Bug Fixes

* make some metrics attributes optional in Gen1
  devices ([b6f79d4](https://github.com/easimon/shelly-exporter/commit/b6f79d47133b073b138c9c90634cda635e81f6e0))

## [2.5.5](https://github.com/easimon/shelly-exporter/compare/2.5.4...2.5.5) (2024-02-17)

### Bug Fixes

* empty commit to test release-please
  4 ([6753b40](https://github.com/easimon/shelly-exporter/commit/6753b40e2cb6bee2727ce1dd38b65b879d61ee1c))
* gen2: make wifi status ip and ssid
  optional ([0439c8d](https://github.com/easimon/shelly-exporter/commit/0439c8db720fe784a23294da333ceb88e973b55b))
* suppress spread operator
  warning ([6da571d](https://github.com/easimon/shelly-exporter/commit/6da571d610c6168588579e0f485ed540a72a7705))

## [2.5.4](https://github.com/easimon/shelly-exporter/compare/v2.5.3...2.5.4) (2024-02-04)

### Bug Fixes

* do not swallow exception on http client
  errors ([ef77464](https://github.com/easimon/shelly-exporter/commit/ef77464ef544b6a4d188287e80f04c45d9fd5ad0))
* fix release
  build ([d4b7b5a](https://github.com/easimon/shelly-exporter/commit/d4b7b5abcc3b921b2035ddbe985a8bfa379521ee))
* fix release-please
  build ([467606d](https://github.com/easimon/shelly-exporter/commit/467606d622fc5fd166eb83697e33492262d67df6))
* fix release-please
  build ([b771917](https://github.com/easimon/shelly-exporter/commit/b7719174509ebf3d23cb12f6c5c4f6d8037b5a17))
* oh fuck you,
  release-please ([5174722](https://github.com/easimon/shelly-exporter/commit/517472233a80c9b97ea0c311233fdd6fb6e8cc66))
* release please action 4
  sucks ([42a5d2f](https://github.com/easimon/shelly-exporter/commit/42a5d2f655c926f20f194c573b12f71aa8d53b9b))
* release please action 4
  sucks ([cda5b95](https://github.com/easimon/shelly-exporter/commit/cda5b9506d1426b601a845be69bad05a34b6d867))
* release-please ([4604752](https://github.com/easimon/shelly-exporter/commit/4604752141fc3aa58dd65bdd6ebf17f3447f748d))
* release-please ([4b86837](https://github.com/easimon/shelly-exporter/commit/4b86837297c29227d7fb8ea9458820aa145d0260))

## [2.5.3](https://github.com/easimon/shelly-exporter/compare/shelly-exporter-v2.5.2...shelly-exporter-2.5.3) (2024-02-04)

### Bug Fixes

* do not swallow exception on http client
  errors ([ef77464](https://github.com/easimon/shelly-exporter/commit/ef77464ef544b6a4d188287e80f04c45d9fd5ad0))
* upgrade to release-please 4.0

## [2.5.2](https://github.com/easimon/shelly-exporter/compare/2.5.1...v2.5.2) (2024-02-04)

### Bug Fixes

**BROKEN RELEASE PIPELINE, USE 2.5.3 INSTEAD**

* do not swallow exception on http client
  errors ([ef77464](https://github.com/easimon/shelly-exporter/commit/ef77464ef544b6a4d188287e80f04c45d9fd5ad0))

## [2.5.1](https://github.com/easimon/shelly-exporter/compare/2.5.0...2.5.1) (2023-11-06)

### Bug Fixes

* trigger release for dependency
  updates ([3fcbe2d](https://github.com/easimon/shelly-exporter/commit/3fcbe2d137c6a9db0ee44a3fc7d02039e97e633b))
* trigger release for dependency
  updates ([f9ee2fa](https://github.com/easimon/shelly-exporter/commit/f9ee2fa0c8ef49d4b8d1f9755dcea82d85e304a7))

## [2.5.0](https://github.com/easimon/shelly-exporter/compare/2.4.0...2.5.0) (2023-06-27)

### Features

* add support for gen 2 devices (3EM
  pro) ([6f7eb82](https://github.com/easimon/shelly-exporter/commit/6f7eb824ad2a54e15f166f417707371a9ff5f6c6))

## [2.4.0](https://github.com/easimon/shelly-exporter/compare/2.3.0...2.4.0) (2023-06-08)

### Features

* add metrics for Wifi and MQTT
  status ([fdd69a2](https://github.com/easimon/shelly-exporter/commit/fdd69a29bcd34e393775a065224d017e1ae6cd6d))

### Bug Fixes

* temperature in fahrenheit shows in metrics
  now ([6b5067b](https://github.com/easimon/shelly-exporter/commit/6b5067b4fada357ac6fdbaf79b523cd5e2d33629))

## [2.3.0](https://github.com/easimon/shelly-exporter/compare/2.2.1...2.3.0) (2023-04-13)

### Features

* do not emit metrics for unreachable
  devices ([e2a2992](https://github.com/easimon/shelly-exporter/commit/e2a29926c2724029065723c3736f1083aa25f890))

## [2.2.1](https://github.com/easimon/shelly-exporter/compare/2.2.0...2.2.1) (2023-02-13)

### Bug Fixes

* watt-minute meters must have different
  name ([97abedd](https://github.com/easimon/shelly-exporter/commit/97abedd2df2fcd72e7f8ba3fa4855eab7f77c689))

## [2.2.0](https://github.com/easimon/shelly-exporter/compare/2.1.0...2.2.0) (2023-02-13)

### Features

* expose power consumption in watt-minutes and watt-hours (
  calculated) ([86025a9](https://github.com/easimon/shelly-exporter/commit/86025a94a69c7d456ebd3ce9ef876b19d9ecf45d))

### Bug Fixes

* migrate deprecated spring boot
  property ([a001290](https://github.com/easimon/shelly-exporter/commit/a001290ca56ac8978dd2b2dadc1b429058676270))

## [2.1.0](https://github.com/easimon/shelly-exporter/compare/2.0.0...2.1.0) (2023-01-30)

### Features

* add base url
  redirect ([202d67e](https://github.com/easimon/shelly-exporter/commit/202d67ead6ee75fb59410d80b864640205d660fa))

## [2.0.0](https://github.com/easimon/shelly-exporter/compare/1.1.0...2.0.0) (2022-12-30)

### ⚠ BREAKING CHANGES

* upgrade to spring boot 3

### Features

* upgrade to spring boot
  3 ([4118aa2](https://github.com/easimon/shelly-exporter/commit/4118aa2d9c8dd693396f34060affa2ecf9802b4d))

## [1.1.0](https://github.com/easimon/shelly-exporter/compare/1.0.2...1.1.0) (2022-10-16)

### Features

* add meter
  descriptions. ([1e9b3d6](https://github.com/easimon/shelly-exporter/commit/1e9b3d61c708525658dc56805521959264f57c21))

## [1.0.2](https://github.com/easimon/shelly-exporter/compare/1.0.1...1.0.2) (2022-09-28)

### Bug Fixes

* http client: make scraping errors cacheable as well (by returning null instead of
  throwing) ([9bdea6b](https://github.com/easimon/shelly-exporter/commit/9bdea6ba0a0a01f316661a1533a9e951a63772cb))
* revert emitting 0 on scraping failure, emit NaN
  instead. ([ec0044d](https://github.com/easimon/shelly-exporter/commit/ec0044d96ea307f98e98646e2cda0d5bf7db10df))

## [1.0.1](https://github.com/easimon/shelly-exporter/compare/1.0.0...1.0.1) (2022-09-19)

### Bug Fixes

* make scraping error result value configurable, default to
  0.0 ([830170d](https://github.com/easimon/shelly-exporter/commit/830170d0ecfea0d9cdc449bfbdcafd786d3e41a9))
* trim device name in registry as
  well ([c307bd2](https://github.com/easimon/shelly-exporter/commit/c307bd284fd50dac254bed0243728a67c5936317))

## [1.0.0](https://github.com/easimon/shelly-exporter/compare/v1.0.1...1.0.0) (2022-09-11)

### Bug Fixes

* export watt-hours correctly (was
  watt-minutes) ([7fce810](https://github.com/easimon/shelly-exporter/commit/7fce810b5eac93f57a3ec92caeaf2a89befe523e))
* trigger tagged build, try
  2 ([fa855b5](https://github.com/easimon/shelly-exporter/commit/fa855b5b5e1bf8ec357b5576a0433771a0d6fde5))
* trigger tagged docker build on
  release ([84473f7](https://github.com/easimon/shelly-exporter/commit/84473f734ac87c8c9bf5477c138b87d7ae879fbe))
* trim label
  values ([0fe560f](https://github.com/easimon/shelly-exporter/commit/0fe560fa3ea69608278e7e9a82f84a9d7dd48ff3))

### Miscellaneous Chores

* release 1.0.0 ([4e421f0](https://github.com/easimon/shelly-exporter/commit/4e421f0f2dfd322bf1a649a0cdc3d842238f4b45))

## Changelog
