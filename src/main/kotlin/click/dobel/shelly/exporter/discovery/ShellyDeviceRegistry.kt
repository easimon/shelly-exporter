package click.dobel.shelly.exporter.discovery

import click.dobel.shelly.exporter.config.ShellyConfigProperties
import click.dobel.shelly.exporter.metrics.ShellyMetrics
import mu.KLoggable
import java.net.Inet4Address

abstract class ShellyDeviceRegistry(
  private val devicesConfig: ShellyConfigProperties.Devices,
  private val metrics: ShellyMetrics<*>,
  private val resolver: AddressResolver,
  logging: KLoggable,
) {
  private val logger = logging.logger

  private val devices: MutableSet<ShellyDevice> = mutableSetOf()

  abstract fun addressToDevice(address: String): ShellyDevice?

  private fun addressToDeviceLogging(address: String): ShellyDevice? {
    return addressToDevice(address).also {
      if (it == null)
        logger.warn { "Could not discover device at [${address}]." }
    }
  }

  private fun discoverDevices(): Set<ShellyDevice> {
    return discoverAddresses()
      .mapNotNull(::addressToDeviceLogging)
      .toSet()
  }

  private fun discoverAddresses(): Set<String> {
    return devicesConfig.hosts
      .flatMap(resolver::resolveToAddresses)
      .filterIsInstance<Inet4Address>()
      .map { it.hostAddress }
      .toSet()
  }

  open fun updateAddresses() {
    logger.info("Updating device addresses.")
    val discovered = discoverDevices()

    val addedDevices: Set<ShellyDevice>
    val removedDevices: Set<ShellyDevice>

    synchronized(devices) {
      addedDevices = discovered - devices
      devices.addAll(addedDevices)

      removedDevices = devices - discovered
      removedDevices.forEach(devices::remove)
    }

    logger.info {
      "Added ${addedDevices.size}, " +
        "removed ${removedDevices.size}, " +
        "current amount: ${devices.size} devices."
    }
    metrics.unregister(removedDevices)
    metrics.register(addedDevices)
  }
}
