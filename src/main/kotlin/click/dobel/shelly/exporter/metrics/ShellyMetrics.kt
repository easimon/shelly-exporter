package click.dobel.shelly.exporter.metrics

import click.dobel.shelly.exporter.client.ShellyClient
import click.dobel.shelly.exporter.config.ShellyConfigProperties
import click.dobel.shelly.exporter.discovery.ShellyDevice
import io.micrometer.core.instrument.FunctionCounter
import io.micrometer.core.instrument.Gauge
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tag
import io.micrometer.core.instrument.Tags
import mu.KLogging
import org.springframework.stereotype.Component

@Component
class ShellyMetrics(
  val shellyClient: ShellyClient,
  val meterRegistry: MeterRegistry,
  configProperties: ShellyConfigProperties,
) {

  companion object : KLogging() {
    const val PREFIX = "shelly."
    const val TAGNAME_CHANNEL = "channel"
  }

  private val failureValue: Double = configProperties.metrics.failureValue

  fun register(devices: Collection<ShellyDevice>) {
    devices.forEach(::register)
  }

  fun unregister(devices: Collection<ShellyDevice>) {
    devices.forEach(::unregister)
  }

  private fun unregister(device: ShellyDevice) {
    logger.info { "Unregistering ${device}." }
    val tags = deviceTags(device).toList()

    meterRegistry.meters
      .filter { it.id.tags.withoutChannel() == tags }
      .map { it.id }
      .forEach {
        logger.debug { "Unregistering meter [${it}]" }
        meterRegistry.remove(it)
      }
  }

  private fun Iterable<Tag>.withoutChannel(): List<Tag> {
    return this.filter { it.key != TAGNAME_CHANNEL }
  }

  private fun register(device: ShellyDevice) {
    logger.info { "Registering ${device}." }
    with(device) {
      val tags = deviceTags(device)

      gauge(
        "power.max",
        "Maximum allowed power consumption before the device switches off.",
        "watts",
        tags
      ) { settings(address)?.maxPower }
      boolGauge(
        "cloud.enabled",
        "Whether Shelly cloud is enabled.",
        tags
      ) { status(address)?.cloud?.isEnabled }
      boolGauge(
        "cloud.connected",
        "Whether Shelly cloud is connected.",
        tags
      ) { status(address)?.cloud?.isConnected }

      boolGauge(
        "wifi.connected",
        "Whether Shelly is connected to WIFI.",
        tags
      ) { status(address)?.wifiSta?.isConnected }
      gauge(
        "wifi.rssi",
        "Current Wifi Received Signal Strength Indication (RSSI).",
        "dbmw",
        tags
      ) { status(address)?.wifiSta?.rssi }
      boolGauge(
        "wifi.roaming.enabled",
        "Whether AP roaming is enabled.",
        tags
      ) { settings(address)?.apRoaming?.isEnabled }
      gauge(
        "wifi.roaming.threshold",
        "RSSI signal strength value below which the device will periodically scan for better access point.",
        "dbmw",
        tags
      ) { settings(address)?.apRoaming?.threshold }

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
      ) { settings(address)?.lat }
      gauge(
        "location.longitude",
        "Longitude of the configured device location, in degrees.",
        "degrees",
        tags
      ) { settings(address)?.lng }

      gauge(
        "temperature.degrees.celsius", // FIXME: unit should be in base unit, but collides with fahrenheit then
        "Device temperature in degrees celsius.",
        "",
        tags
      ) { status(address)?.temperature?.celsius }
      gauge(
        "temperature.degrees.fahrenheit", // FIXME: unit should be in base unit, but collides with celsius then
        "Device temperature in degrees fahrenheit.",
        "",
        tags
      ) { status(address)?.temperature?.fahrenheit }
      boolGauge(
        "temperature.valid",
        "Whether the current temperature reading is valid.",
        tags
      ) { status(address)?.temperature?.isValid }
      boolGauge(
        "temperature.overheated",
        "Whether the device is overheated.",
        tags
      ) { status(address)?.overTemperature }

      boolGauge(
        "update.available",
        "Whether a firmware update is available for the device.",
        tags
      ) { status(address)?.update?.hasUpdate }
      counter(
        "uptime",
        "Device uptime in seconds.",
        "seconds",
        tags
      ) { status(address)?.uptime }
      gauge(
        "filesystem.free",
        "Filesystem free space in bytes.",
        "bytes",
        tags
      ) { status(address)?.fileSystemFree }
      gauge(
        "filesystem.size",
        "Filesystem total capacity in bytes.",
        "bytes",
        tags
      ) { status(address)?.fileSystemSize }
      gauge(
        "memory.free",
        "Free memory in bytes.",
        "bytes",
        tags
      ) { status(address)?.ramFree }
      gauge(
        "memory.size",
        "Total memory size in bytes.",
        "bytes",
        tags
      ) { status(address)?.ramTotal }
      gauge(
        "memory.low-water-mark",
        "Memory low water mark in bytes.",
        "bytes",
        tags
      ) { status(address)?.ramLowWaterMark }

      val meterCount = catchingWithDefault(0) {
        shellyClient.status(address)?.meters?.size
      }
      for (index in 0 until meterCount) {
        val meterTags = tags.and(Tag.of(TAGNAME_CHANNEL, index.toString()))

        counter(
          "meter.power",
          "Total power consumption in watt-hours.",
          "watthours",
          meterTags
        ) { (status(address)?.meters?.get(index)?.wattHoursTotal ?: Double.NaN) }
        counter(
          "meter.power.native",
          "Total power consumption in watt-minutes.",
          "wattminutes",
          meterTags
        ) { (status(address)?.meters?.get(index)?.wattMinutesTotal ?: Double.NaN) }
        gauge(
          "meter.power.current",
          "Momentary power consumption in watts.",
          "watts",
          meterTags
        ) { status(address)?.meters?.get(index)?.power }
        gauge(
          "meter.overpower",
          "Momentary power over-consumption in watts.",
          "watts",
          meterTags
        ) { status(address)?.meters?.get(index)?.overpower }
        boolGauge(
          "meter.value.valid",
          "Whether momentary readings are valid.",
          meterTags
        ) { status(address)?.meters?.get(index)?.isValid }
      }

      val outputCount = catchingWithDefault(0) {
        shellyClient.status(address)?.relays?.size
      }
      for (index in 0 until outputCount) {
        val relayTags = tags.and(Tag.of(TAGNAME_CHANNEL, index.toString()))

        boolGauge(
          "relay.on",
          "Whether the relay is switched on.",
          relayTags
        ) { status(address)?.relays?.get(index)?.isOn }
        boolGauge(
          "relay.overpower",
          "Whether the relay has detected overpower / overload.",
          relayTags
        ) { status(address)?.relays?.get(index)?.overpower }
        boolGauge(
          "relay.has-timer",
          "Whether the relay has a timer set.",
          relayTags
        ) { status(address)?.relays?.get(index)?.hasTimer }
        gauge(
          "relay.timer-started",
          "Seconds since the current relay timer has started, if any.",
          "seconds",
          relayTags
        ) { status(address)?.relays?.get(index)?.timerStarted }
        gauge(
          "relay.timer-duration",
          "Total duration of current relay timer, if any.",
          "seconds",
          relayTags
        ) { status(address)?.relays?.get(index)?.timerDuration }
        gauge(
          "relay.timer-remaining",
          "Remaining seconds of the current relay timer.",
          "seconds",
          relayTags
        ) { status(address)?.relays?.get(index)?.timerRemaining }
      }
    }
  }

  private inline fun ShellyDevice.counter(
    name: String,
    description: String,
    baseUnit: String?,
    tags: Tags,
    crossinline func: ShellyClient.() -> Number?
  ) {
    val counterId = FunctionCounter
      .builder(pre(name), this) { shellyClient.runCatching { func() }.getOrNull().orDefault() }
      .description(description)
      .baseUnit(baseUnit.trimToNull())
      .tags(tags)
      .register(meterRegistry).id

    logger.debug {
      "Registered meter [${counterId}]"
    }
  }

  private inline fun ShellyDevice.gauge(
    name: String,
    description: String,
    baseUnit: String?,
    tags: Tags,
    crossinline func: ShellyClient.() -> Number?
  ) {
    val gaugeId = Gauge
      .builder(pre(name), this) { shellyClient.runCatching { func() }.getOrNull().orDefault() }
      .description(description)
      .baseUnit(baseUnit.trimToNull())
      .tags(tags)
      .register(meterRegistry).id

    logger.debug { "Registered meter [${gaugeId}]" }
  }

  private fun pre(s: String) = "$PREFIX$s"

  private inline fun ShellyDevice.boolGauge(
    name: String,
    description: String,
    tags: Tags,
    crossinline func: ShellyClient.() -> Boolean?
  ) {
    gauge(name, description, null, tags) { func().toDouble() }
  }

  private fun Number?.orDefault(): Double = this?.toDouble() ?: failureValue

  private fun Boolean?.toDouble(): Double = when (this) {
    true -> 1.0
    false -> 0.0
    else -> failureValue
  }

  private fun deviceTags(device: ShellyDevice): Tags {
    return Tags.of(
      Tag.of("name", device.name.trim()),
      Tag.of("address", device.address.trim()),
      Tag.of("mac", device.mac.trim()),
      Tag.of("type", device.type.trim()),
      Tag.of("firmwareVersion", device.firmwareVersion.trim()),
    )
  }

  private inline fun <R> catching(block: () -> R): Result<R> {
    return try {
      Result.success(block())
    } catch (e: Throwable) {
      Result.failure(e)
    }
  }

  private inline fun <AR : Any, R : AR?> catchingWithDefault(default: AR, block: () -> R): AR {
    return catching(block).getOrNull() ?: default
  }

  private fun String?.trimToNull() = if (this.isNullOrBlank()) null else this
}
