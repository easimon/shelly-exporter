package click.dobel.shelly.exporter.metrics

import click.dobel.shelly.exporter.ShellyExporterConfiguration
import click.dobel.shelly.exporter.client.ShellyClient
import click.dobel.shelly.exporter.discovery.ShellyDevice
import io.micrometer.core.instrument.FunctionCounter
import io.micrometer.core.instrument.Gauge
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tag
import io.micrometer.core.instrument.Tags
import mu.KLoggable

abstract class ShellyMetrics<T : ShellyClient>(
  val client: T,
  val meterRegistry: MeterRegistry,
  loggable: KLoggable
) {
  companion object {
    const val PREFIX = "shelly."
    const val TAGNAME_CHANNEL = "channel"

    fun pre(s: String) = "${PREFIX}$s"
  }

  private val logger = loggable.logger

  fun register(devices: Collection<ShellyDevice>) {
    devices.forEach(::register)
  }

  fun unregister(devices: Collection<ShellyDevice>) {
    devices.forEach(::unregister)
  }

  protected abstract fun register(device: ShellyDevice)

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

  protected fun ShellyDevice.counter(
    name: String,
    description: String,
    baseUnit: String?,
    tags: Tags,
    func: T.() -> Number?
  ) {
    val counterId = FunctionCounter
      .builder(pre(name), this) { client.runCatching { func() }.getOrNull().orDefault() }
      .description(description)
      .baseUnit(baseUnit.trimToNull())
      .tags(tags)
      .register(meterRegistry).id

    logger.debug {
      "Registered meter [${counterId}]"
    }
  }

  protected fun ShellyDevice.gauge(
    name: String,
    description: String,
    baseUnit: String?,
    tags: Tags,
    func: T.() -> Number?
  ) {
    val gaugeId = Gauge
      .builder(pre(name), this) { client.runCatching { func() }.getOrNull().orDefault() }
      .description(description)
      .baseUnit(baseUnit.trimToNull())
      .tags(tags)
      .register(meterRegistry).id

    logger.debug { "Registered meter [${gaugeId}]" }
  }

  protected fun ShellyDevice.boolGauge(
    name: String,
    description: String,
    tags: Tags,
    func: T.() -> Boolean?
  ) {
    gauge(name, description, null, tags) { func().toDouble() }
  }

  private fun Number?.orDefault(): Double = this?.toDouble() ?: ShellyExporterConfiguration.SCRAPE_FAILURE_VALUE

  private fun Boolean?.toDouble(): Double = when (this) {
    true -> 1.0
    false -> 0.0
    else -> ShellyExporterConfiguration.SCRAPE_FAILURE_VALUE
  }

  protected fun deviceTags(device: ShellyDevice): Tags {
    return Tags.of(
      Tag.of("name", device.name.trim()),
      Tag.of("address", device.address.trim()),
      Tag.of("mac", device.mac.trim()),
      Tag.of("type", device.type.trim()),
      Tag.of("firmwareVersion", device.firmwareVersion.trim()),
    )
  }

  protected inline fun <R> catching(block: () -> R): Result<R> {
    return try {
      Result.success(block())
    } catch (e: Throwable) {
      Result.failure(e)
    }
  }

  protected inline fun <AR : Any, R : AR?> catchingWithDefault(default: AR, block: () -> R): AR {
    return catching(block).getOrNull() ?: default
  }

  protected fun String?.trimToNull() = if (this.isNullOrBlank()) null else this
}
