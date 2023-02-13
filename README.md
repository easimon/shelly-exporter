# Shelly prometheus exporter

## Introduction

This is a simple Prometheus exporter for Shelly Gen1 devices. It retrieves power consumption, state etc. via local
Shelly HTTP API and presents them in a Prometheus compatible format. Since it uses the local HTTP API,
the exporter does not require Shelly cloud (but runs fine alongside).

## Supported devices

At the moment, this exporter is tested only with *Shelly Plug* and *Shelly Plug S*, since this is what I own.
Other series *might* be supported as well, since the HTTP API is similar (not identical) across the whole Shelly family.
If you have a different Shelly device, please tell me if it works or not.

## Running

Choose one of the options below to run the exporter. The server then listens on port 8080 (plain HTTP), prometheus
metrics are available at `http://host:8080/prometheus`.

### Configuration

The minimal required configuration is a valid Shelly device host name or IP address. For complete list of configurable
items and their defaults, see the [application.yaml](./src/main/resources/application.yaml)

By default, Shelly devices allow unauthenticated access to the local HTTP API. If you protected the local API with a
username and password, it needs to be the same across all devices. This is not the Shelly cloud username and password.

| Environment variable              | Description                                             | Default | Required |
|-----------------------------------|---------------------------------------------------------|---------|----------|
| SHELLY_DEVICES_HOSTS              | Comma-separated list of hostnames and/or IP addresses   | (none)  | yes      |
| SHELLY_AUTH_USERNAME              | Shelly HTTP API username (must be same for all devices) | (none)  | no       |
| SHELLY_AUTH_PASSWORD              | Shelly HTTP API password (must be same for all devices) | (none)  | no       |
| SHELLY_DEVICES_DISCOVERY_INTERVAL | Interval to start a device discovery                    | 5 min   | no       |

#### Device discovery

The exporter tries to discover Shelly devices via DNS and IP Addresses in regular intervals by accessing the HTTP API.
Metrics will be exposed only for responding devices. So if you unplug / replug a Shelly, its metrics will vanish and
reappear after the discovery interval. Discovery failures are logged with a reason. Failure reasons can be

- device not responding
- authentication failure (username/password incorrect)

If you have the possibility to create a single DNS A record that contains all Shelly device addresses, this is a
supported scenario (and actually how I run the exporter):

```bash
$ dig shellies

...
;; QUESTION SECTION:
;shellies.			IN	A

;; ANSWER SECTION:
shellies.		60	IN	A	192.168.x.12
shellies.		60	IN	A	192.168.x.23
shellies.		60	IN	A	192.168.x.34
...
```

### Building and running an executable JAR

Check out this repository, build the application using `./mvnw package`, and then run the following command:

```bash
$ SHELLY_AUTH_USERNAME=your_username \
  SHELLY_AUTH_PASSWORD=your_password \
  SHELLY_DEVICES_HOSTS=shelly1.local,192.168.0.5 \
  java -jar target/shelly-*.jar
```

### Building and running the docker container

Optional: To build the docker container yourself, check out this repository and build it using

```bash
$ export SHELLY_EXPORTER_IMAGE=ghcr.io/easimon/shelly-exporter:v2.2.1 # x-release-please-version
$ docker build --tag $SHELLY_EXPORTER_IMAGE .
```

Running it

```bash
$ export SHELLY_EXPORTER_IMAGE=ghcr.io/easimon/shelly-exporter:v2.2.1 # x-release-please-version
$ docker run \
  -e SHELLY_AUTH_USERNAME=your_username \
  -e SHELLY_AUTH_PASSWORD=your_password \
  -e SHELLY_DEVICES_HOSTS=shelly1.local,192.168.0.5 \
  -p '8080:8080' \
  $SHELLY_EXPORTER_IMAGE
```

### Prometheus scraping config

```yaml
scrape_configs:
  - job_name: shelly-exporter
    metrics_path: /prometheus
    scheme: http
    static_configs:
      - targets:
          - shelly-exporter:8080
```

### Available metrics

Defined in [ShellyMetrics.kt](src/main/kotlin/click/dobel/shelly/exporter/metrics/ShellyMetrics.kt).
All metrics contain tags for

- `address`: IP address
- `mac`: MAC address
- `type`: Device Type
- `name`: Device Name (the one stored on the device)
- `firmwareVersion`: Firmware Version

Some metrics contain an additional tag `channel`, which is always 0 for Shelly Plugs. For devices with multiple meters
and / or relays, there is one for each channel.

Example when scraping a single plug (TODO: add description for each metric):

```prometheus
## General
# TYPE shelly_uptime_seconds_total counter
shelly_uptime_seconds_total{address="192.168.123.123",firmwareVersion="20220809-124506/v1.12-g99f7e0b",mac="AABBCCDDEEFF",name="Kitchen Light",type="SHPLG-S",} 859951.0
# TYPE shelly_power_max_watts gauge
shelly_power_max_watts{address="192.168.123.123",firmwareVersion="20220809-124506/v1.12-g99f7e0b",mac="AABBCCDDEEFF",name="Kitchen Light",type="SHPLG-S",} 1800.0

### Shelly embedded system stats
# TYPE shelly_memory_size_bytes gauge
shelly_memory_size_bytes{address="192.168.123.123",firmwareVersion="20220809-124506/v1.12-g99f7e0b",mac="AABBCCDDEEFF",name="Kitchen Light",type="SHPLG-S",} 52072.0
# TYPE shelly_memory_free_bytes gauge
shelly_memory_free_bytes{address="192.168.123.123",firmwareVersion="20220809-124506/v1.12-g99f7e0b",mac="AABBCCDDEEFF",name="Kitchen Light",type="SHPLG-S",} 40108.0
# TYPE shelly_memory_low_water_mark_bytes gauge
shelly_memory_low_water_mark_bytes{address="192.168.123.123",firmwareVersion="20220809-124506/v1.12-g99f7e0b",mac="AABBCCDDEEFF",name="Kitchen Light",type="SHPLG-S",} 0.0
# TYPE shelly_filesystem_size_bytes gauge
shelly_filesystem_size_bytes{address="192.168.123.123",firmwareVersion="20220809-124506/v1.12-g99f7e0b",mac="AABBCCDDEEFF",name="Kitchen Light",type="SHPLG-S",} 233681.0
# TYPE shelly_filesystem_free_bytes gauge
shelly_filesystem_free_bytes{address="192.168.123.123",firmwareVersion="20220809-124506/v1.12-g99f7e0b",mac="AABBCCDDEEFF",name="Kitchen Light",type="SHPLG-S",} 166664.0

### Temperature (Shelly Plug S only)
# TYPE shelly_temperature_degrees_celsius gauge
shelly_temperature_degrees_celsius{address="192.168.123.123",firmwareVersion="20220809-124506/v1.12-g99f7e0b",mac="AABBCCDDEEFF",name="Kitchen Light",type="SHPLG-S",} 30.65
# TYPE shelly_temperature_overheated gauge
shelly_temperature_overheated{address="192.168.123.123",firmwareVersion="20220809-124506/v1.12-g99f7e0b",mac="AABBCCDDEEFF",name="Kitchen Light",type="SHPLG-S",} 0.0
# TYPE shelly_temperature_valid gauge
shelly_temperature_valid{address="192.168.123.123",firmwareVersion="20220809-124506/v1.12-g99f7e0b",mac="AABBCCDDEEFF",name="Kitchen Light",type="SHPLG-S",} 1.0

### Cloud
# TYPE shelly_cloud_enabled gauge
shelly_cloud_enabled{address="192.168.123.123",firmwareVersion="20220809-124506/v1.12-g99f7e0b",mac="AABBCCDDEEFF",name="Kitchen Light",type="SHPLG-S",} 1.0
# TYPE shelly_cloud_connected gauge
shelly_cloud_connected{address="192.168.123.123",firmwareVersion="20220809-124506/v1.12-g99f7e0b",mac="AABBCCDDEEFF",name="Kitchen Light",type="SHPLG-S",} 1.0


## Meters
# TYPE shelly_meter_power_current_watts gauge
shelly_meter_power_current_watts{address="192.168.123.123",channel="0",firmwareVersion="20220809-124506/v1.12-g99f7e0b",mac="AABBCCDDEEFF",name="Kitchen Light",type="SHPLG-S",} 49.49
# TYPE shelly_meter_power_watthours_total counter
shelly_meter_power_watthours_total{address="192.168.123.123",channel="0",firmwareVersion="20220809-124506/v1.12-g99f7e0b",mac="AABBCCDDEEFF",name="Kitchen Light",type="SHPLG-S",} 721189.0
# TYPE shelly_meter_overpower_watts gauge
shelly_meter_overpower_watts{address="192.168.123.123",channel="0",firmwareVersion="20220809-124506/v1.12-g99f7e0b",mac="AABBCCDDEEFF",name="Kitchen Light",type="SHPLG-S",} 0.0
# TYPE shelly_meter_value_valid gauge
shelly_meter_value_valid{address="192.168.123.123",channel="0",firmwareVersion="20220809-124506/v1.12-g99f7e0b",mac="AABBCCDDEEFF",name="Kitchen Light",type="SHPLG-S",} 1.0


## Relays
# TYPE shelly_relay_on gauge
shelly_relay_on{address="192.168.123.123",channel="0",firmwareVersion="20220809-124506/v1.12-g99f7e0b",mac="AABBCCDDEEFF",name="Kitchen Light",type="SHPLG-S",} 1.0
# TYPE shelly_relay_overpower gauge
shelly_relay_overpower{address="192.168.123.123",channel="0",firmwareVersion="20220809-124506/v1.12-g99f7e0b",mac="AABBCCDDEEFF",name="Kitchen Light",type="SHPLG-S",} 0.0
# TYPE shelly_relay_has_timer gauge
shelly_relay_has_timer{address="192.168.123.123",channel="0",firmwareVersion="20220809-124506/v1.12-g99f7e0b",mac="AABBCCDDEEFF",name="Kitchen Light",type="SHPLG-S",} 0.0
# TYPE shelly_relay_timer_remaining_seconds gauge
shelly_relay_timer_remaining_seconds{address="192.168.123.123",channel="0",firmwareVersion="20220809-124506/v1.12-g99f7e0b",mac="AABBCCDDEEFF",name="Kitchen Light",type="SHPLG-S",} 0.0
# TYPE shelly_relay_timer_duration_seconds gauge
shelly_relay_timer_duration_seconds{address="192.168.123.123",channel="0",firmwareVersion="20220809-124506/v1.12-g99f7e0b",mac="AABBCCDDEEFF",name="Kitchen Light",type="SHPLG-S",} 0.0
# TYPE shelly_relay_timer_started_seconds gauge
shelly_relay_timer_started_seconds{address="192.168.123.123",channel="0",firmwareVersion="20220809-124506/v1.12-g99f7e0b",mac="AABBCCDDEEFF",name="Kitchen Light",type="SHPLG-S",} 0.0
```

## References

- [Shelly Gen1 Common HTTP API](https://shelly-api-docs.shelly.cloud/gen1/#common-http-api)
- [Shelly Plug /status additions](https://shelly-api-docs.shelly.cloud/gen1/#shelly-plug-plugs-status)
- [Shelly Plug /settings additions](https://shelly-api-docs.shelly.cloud/gen1/#shelly-plug-plugs-settings)

## Disclaimer

This project is not affiliated with Shelly in any way.
