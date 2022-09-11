package click.dobel.shelly.exporter.metrics

import click.dobel.shelly.exporter.client.ShellyClient
import click.dobel.shelly.exporter.discovery.ShellyDevice
import click.dobel.shelly.exporter.logging.logger
import io.micrometer.core.instrument.FunctionCounter
import io.micrometer.core.instrument.Gauge
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tag
import io.micrometer.core.instrument.Tags
import org.springframework.stereotype.Component

@Component
class ShellyMetrics(
  val shellyClient: ShellyClient,
  val meterRegistry: MeterRegistry
) {

  companion object {
    val LOG = logger()
    const val PREFIX = "shelly."

    const val TAGNAME_CHANNEL = "channel"
  }

  fun register(devices: Collection<ShellyDevice>) {
    devices.forEach(::register)
  }

  fun unregister(devices: Collection<ShellyDevice>) {
    devices.forEach(::unregister)
  }

  private fun unregister(device: ShellyDevice) {
    LOG.info("Unregistering {}.", device)
    val tags = deviceTags(device).toList()

    meterRegistry.meters
      .filter { it.id.tags.withoutChannel() == tags }
      .map { it.id }
      .forEach {
        LOG.debug("Unregistering meter [{}]", it)
        meterRegistry.remove(it)
      }
  }

  private fun Iterable<Tag>.withoutChannel(): List<Tag> {
    return this.filter { it.key != TAGNAME_CHANNEL }
  }

  private fun register(device: ShellyDevice) {
    LOG.info("Registering {}.", device)
    with(device) {
      val tags = deviceTags(device)

      gauge("power.max", "watts", tags) { settings(address).maxPower }
      boolGauge("cloud.enabled", tags) { settings(address).cloud.enabled }
      boolGauge("cloud.connected", tags) { settings(address).cloud.connected }

      gauge("temperature", "degrees.celsius", tags) { status(address).temperature?.celsius }
      gauge("temperature", "degrees.fahrenheit", tags) { status(address).temperature?.fahrenheit }
      boolGauge("temperature.valid", tags) { status(address).temperature?.isValid }
      boolGauge("temperature.overheated", tags) { status(address).overTemperature }

      boolGauge("update.available", tags) { status(address).update.hasUpdate }
      counter("uptime", "seconds", tags) { status(address).uptime }
      gauge("filesystem.free", "bytes", tags) { status(address).fileSystemFree }
      gauge("filesystem.size", "bytes", tags) { status(address).fileSystemSize }
      gauge("memory.free", "bytes", tags) { status(address).ramFree }
      gauge("memory.size", "bytes", tags) { status(address).ramTotal }
      gauge("memory.low-water-mark", "bytes", tags) { status(address).ramLowWaterMark }

      val meterCount = catchingWithDefault(0) {
        shellyClient.status(address).meters.size
      }
      for (index in 0 until meterCount) {
        val meterTags = tags.and(Tag.of(TAGNAME_CHANNEL, index.toString()))

        counter("meter.power", "watthours", meterTags) { status(address).meters[index].total }
        gauge("meter.power.current", "watts", meterTags) { status(address).meters[index].power }
        gauge("meter.overpower", "watts", meterTags) { status(address).meters[index].overpower }
        boolGauge("meter.value.valid", meterTags) { status(address).meters[index].isValid }
      }

      val outputCount = catchingWithDefault(0) {
        shellyClient.status(address).relays.size
      }
      for (index in 0 until outputCount) {
        val relayTags = tags.and(Tag.of(TAGNAME_CHANNEL, index.toString()))

        boolGauge("relay.on", relayTags) { status(address).relays[index].isOn }
        boolGauge("relay.overpower", relayTags) { status(address).relays[index].overpower }
        boolGauge("relay.has-timer", relayTags) { status(address).relays[index].hasTimer }
        gauge("relay.timer-started", "seconds", relayTags) { status(address).relays[index].timerStarted }
        gauge("relay.timer-duration", "seconds", relayTags) { status(address).relays[index].timerDuration }
        gauge("relay.timer-remaining", "seconds", relayTags) { status(address).relays[index].timerRemaining }
      }
    }
  }

  private inline fun ShellyDevice.counter(
    name: String,
    baseUnit: String?,
    tags: Tags,
    crossinline func: ShellyClient.() -> Number?
  ) {
    LOG.debug(
      "Registered meter [{}]",
      FunctionCounter
        .builder(pre(name), this) { shellyClient.runCatching { func() }.getOrNull().nullToNaN() }
        .baseUnit(baseUnit)
        .tags(tags)
        .register(meterRegistry).id
    )
  }

  private inline fun ShellyDevice.gauge(
    name: String,
    baseUnit: String?,
    tags: Tags,
    crossinline func: ShellyClient.() -> Number?
  ) {
    LOG.debug(
      "Registered meter [{}]",
      Gauge
        .builder(pre(name), this) { shellyClient.runCatching { func() }.getOrNull().nullToNaN() }
        .baseUnit(baseUnit)
        .tags(tags)
        .register(meterRegistry).id
    )
  }

  private fun pre(s: String) = "$PREFIX$s"

  private inline fun ShellyDevice.boolGauge(
    name: String,
    tags: Tags,
    crossinline func: ShellyClient.() -> Boolean?
  ) {
    gauge(name, null, tags) { func().toDouble() }
  }

  private fun Number?.nullToNaN(): Double = this?.toDouble() ?: Double.NaN

  private fun Boolean?.toDouble(): Double = when (this) {
    true -> 1.0
    false -> 0.0
    else -> Double.NaN
  }

  private fun deviceTags(device: ShellyDevice): Tags {
    return Tags.of(
      Tag.of("name", device.name),
      Tag.of("address", device.address),
      Tag.of("mac", device.mac),
      Tag.of("type", device.type),
      Tag.of("firmwareVersion", device.firmwareVersion),
    )
  }

  private inline fun <R> catching(block: () -> R): Result<R> {
    return try {
      Result.success(block())
    } catch (e: Throwable) {
      Result.failure(e)
    }
  }

  private inline fun <R> catchingWithDefault(default: R, block: () -> R): R {
    return catching(block).getOrDefault(default)
  }
}
