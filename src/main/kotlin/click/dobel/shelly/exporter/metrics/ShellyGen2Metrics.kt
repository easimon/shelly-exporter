package click.dobel.shelly.exporter.metrics

import click.dobel.shelly.exporter.client.ShellyGen2Client
import click.dobel.shelly.exporter.discovery.ShellyDevice
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tag
import mu.KLogging
import org.springframework.stereotype.Component

@Component
class ShellyGen2Metrics(
  client: ShellyGen2Client,
  meterRegistry: MeterRegistry,
) : ShellyMetrics<ShellyGen2Client>(
  client,
  meterRegistry,
  Companion
) {
  companion object : KLogging()

  override fun register(device: ShellyDevice) {
    logger.info { "Registering ${device}." }
    with(device) {
      val tags = deviceTags(device)

      // General
      boolGauge(
        "cloud.enabled",
        "Whether Shelly cloud is enabled.",
        tags
      ) { config(address)?.cloud?.isEnabled }
      boolGauge(
        "cloud.connected",
        "Whether Shelly cloud is connected.",
        tags
      ) { status(address)?.cloud?.isConnected }

      boolGauge(
        "wifi.connected",
        "Whether Shelly is connected to WIFI.",
        tags
      ) { status(address)?.wifi?.status == "got ip" }
      gauge(
        "wifi.rssi",
        "Current Wifi Received Signal Strength Indication (RSSI).",
        "dbmw",
        tags
      ) { status(address)?.wifi?.rssi }
      boolGauge(
        "wifi.roaming.enabled",
        "Whether AP roaming is enabled.",
        tags
      ) { config(address)?.wifi?.roam?.isEnabled }
      gauge(
        "wifi.roaming.threshold",
        "RSSI signal strength value below which the device will periodically scan for better access point.",
        "dbmw",
        tags
      ) { config(address)?.wifi?.roam?.rssiThreshold }

      boolGauge(
        "mqtt.connected",
        "Whether Shelly is connected to MQTT server.",
        tags
      ) { status(address)?.mqtt?.isConnected }

      gauge(
        "location.latitude",
        "Latitude of the configured device location, in degrees.",
        "degrees",
        tags
      ) { config(address)?.sys?.location?.lat }
      gauge(
        "location.longitude",
        "Longitude of the configured device location, in degrees.",
        "degrees",
        tags
      ) { config(address)?.sys?.location?.lng }

      // Shelly Plus 3EM has device temperature.
      // Tag with channel "main" for compatibility with switch temperatures (channels 0...)
      val tempTags = tags.and(Tag.of(TAGNAME_CHANNEL, "main"))
      gauge(
        "temperature.degrees.celsius", // FIXME: unit should be in base unit, but collides with fahrenheit then
        "Device temperature in degrees celsius.",
        "",
        tempTags
      ) { status(address)?.temperature?.celsius }
      gauge(
        "temperature.degrees.fahrenheit", // FIXME: unit should be in base unit, but collides with celsius then
        "Device temperature in degrees fahrenheit.",
        "",
        tempTags
      ) { status(address)?.temperature?.fahrenheit }

      boolGauge(
        "update.available",
        "Whether a firmware update is available for the device.",
        tags
      ) { status(address)?.sys?.availableUpdates?.stable?.version != null }

      counter(
        "uptime",
        "Device uptime in seconds.",
        "seconds",
        tags
      ) { status(address)?.sys?.uptime }
      gauge(
        "filesystem.free",
        "Filesystem free space in bytes.",
        "bytes",
        tags
      ) { status(address)?.sys?.fsFree }
      gauge(
        "filesystem.size",
        "Filesystem total capacity in bytes.",
        "bytes",
        tags
      ) { status(address)?.sys?.fsTotal }
      gauge(
        "memory.free",
        "Free memory in bytes.",
        "bytes",
        tags
      ) { status(address)?.sys?.ramFree }
      gauge(
        "memory.size",
        "Total memory size in bytes.",
        "bytes",
        tags
      ) { status(address)?.sys?.ramFree }

      // Shelly Plus 1PM
      val inputTags = tags.and(Tag.of(TAGNAME_CHANNEL, 0.toString()))
      boolGauge(
        "input.on",
        "Whether the input is switched on.",
        inputTags
      ) { status(address)?.input0?.state }

      // Switches, e.g. Shelly Plus 1PM and Plug
      for (index in (client.status(address)?.switches?.keys ?: emptySet())) {
        val switchTags = tags.and(Tag.of(TAGNAME_CHANNEL, index.toString()))

        boolGauge(
          "relay.on",
          "Whether the relay is switched on.",
          switchTags
        ) { status(address)?.switches?.get(index)?.outputEnabled }
        gauge(
          "meter.power.current",
          "Momentary power consumption in watts.",
          "watts",
          switchTags
        ) { status(address)?.switches?.get(index)?.power }
        gauge(
          "meter.voltage.current",
          "Momentary voltage in volts.",
          "volts",
          switchTags
        ) { status(address)?.switches?.get(index)?.voltage }
        gauge(
          "meter.current.current",
          "Momentary current in amperes.",
          "amperes",
          switchTags
        ) { status(address)?.switches?.get(index)?.current }
        counter(
          "meter.power",
          "Total power consumption in watt-hours.",
          "watthours",
          switchTags
        ) { status(address)?.switches?.get(index)?.energy?.total }
        counter(
          "meter.power.returned",
          "Total power production in watt-hours (returned to the grid).",
          "watthours",
          switchTags
        ) { status(address)?.switches?.get(index)?.returnedEnergy?.total }
        gauge(
          "meter.powerfactor",
          "Momentary power factor.",
          "",
          switchTags
        ) { status(address)?.switches?.get(index)?.powerFactor }
        gauge(
          "meter.frequency",
          "Momentary frequency in Hz.",
          "Hz",
          switchTags
        ) { status(address)?.switches?.get(index)?.frequency }
        gauge(
          "temperature.degrees.celsius", // FIXME: unit should be in base unit, but collides with fahrenheit then
          "Device temperature in degrees celsius.",
          "",
          switchTags
        ) { status(address)?.switches?.get(index)?.temperature?.celsius }
        gauge(
          "temperature.degrees.fahrenheit", // FIXME: unit should be in base unit, but collides with celsius then
          "Device temperature in degrees fahrenheit.",
          "",
          switchTags
        ) { status(address)?.switches?.get(index)?.temperature?.fahrenheit }
      }

      // Meters, e.g. Shelly Plus 3EM
      for (index in client.status(address)?.phaseNames ?: emptySet()) {
        val meterTags = tags.and(Tag.of(TAGNAME_CHANNEL, index.metricName))

        counter(
          "meter.power",
          "Total power consumption in watt-hours.",
          "watthours",
          meterTags
        ) { status(address)?.phaseData?.get(index)?.totalActualEnergy }
        counter(
          "meter.power.returned",
          "Total power production in watt-hours (returned to the grid).",
          "watthours",
          meterTags
        ) { status(address)?.phaseData?.get(index)?.totalActualReturnedEnergy }
        gauge(
          "meter.power.current",
          "Momentary power consumption in watts.",
          "watts",
          meterTags
        ) { status(address)?.phaseStatus?.get(index)?.actualPower }
        gauge(
          "meter.power.apparent",
          "Momentary apparent power consumption in watts.",
          "watts",
          meterTags
        ) { status(address)?.phaseStatus?.get(index)?.apparentPower }
        gauge(
          "meter.voltage.current",
          "Momentary voltage in volts.",
          "volts",
          meterTags
        ) { status(address)?.phaseStatus?.get(index)?.voltage }
        gauge(
          "meter.powerfactor",
          "Momentary power factor.",
          "",
          meterTags
        ) { status(address)?.phaseStatus?.get(index)?.powerFactor }
      }
    }
  }
}
