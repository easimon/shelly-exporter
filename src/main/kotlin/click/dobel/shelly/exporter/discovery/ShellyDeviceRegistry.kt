package click.dobel.shelly.exporter.discovery

import click.dobel.shelly.exporter.client.ShellyClient
import click.dobel.shelly.exporter.config.ShellyConfigProperties
import click.dobel.shelly.exporter.metrics.ShellyMetrics
import mu.KLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.net.Inet4Address

@Component
class ShellyDeviceRegistry(
  private val configProperties: ShellyConfigProperties,
  private val shellyClient: ShellyClient,
  private val metrics: ShellyMetrics,
  @Autowired(required = false) private val resolver: AddressResolver = DefaultAddressResolver
) {
  companion object : KLogging()

  val devices: MutableSet<ShellyDevice> = mutableSetOf()

  fun discoverAddresses(): Set<String> {
    return configProperties.devices.hosts
      .flatMap(resolver::resolveToAddresses)
      .filterIsInstance<Inet4Address>()
      .map { it.hostAddress }
      .toSet()
  }

  fun addressToDevice(address: String): ShellyDevice? {
    return shellyClient.runCatching {
      settings(address)!!.run {
        ShellyDevice(
          mac = device.mac,
          address = address,
          name = name.trim(),
          type = device.type,
          firmwareVersion = firmwareVersion,
        )
      }
    }.onFailure { ex ->
      logger.warn { "Could not discover device at [${address}]; ${ex.message}" }
    }.getOrNull()
  }

  fun discoverDevices(): Set<ShellyDevice> {
    return discoverAddresses()
      .mapNotNull(::addressToDevice)
      .toSet()
  }

  @Scheduled(fixedRateString = "\${shelly.devices.discovery-interval:PT1M}")
  fun updateAddresses() {
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
