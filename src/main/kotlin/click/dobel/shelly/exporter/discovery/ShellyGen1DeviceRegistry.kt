package click.dobel.shelly.exporter.discovery

import click.dobel.shelly.exporter.client.ShellyGen1Client
import click.dobel.shelly.exporter.config.ShellyConfigProperties
import click.dobel.shelly.exporter.metrics.ShellyGen1Metrics
import mu.KLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ShellyGen1DeviceRegistry(
  configProperties: ShellyConfigProperties,
  private val shellyClient: ShellyGen1Client,
  metrics: ShellyGen1Metrics,
  @Autowired(required = false) private val resolver: AddressResolver = DefaultAddressResolver
) : ShellyDeviceRegistry(
  configProperties.devices,
  metrics,
  resolver,
  Companion
) {
  companion object : KLogging()

  override fun addressToDevice(address: String): ShellyDevice? {
    return shellyClient.settings(address)?.run {
      ShellyDevice(
        mac = device.mac,
        address = address,
        name = name.trim(),
        type = device.type,
        firmwareVersion = firmwareVersion,
      )
    }
  }

  @Scheduled(fixedRateString = "\${shelly.devices.discovery-interval:PT1M}")
  override fun updateAddresses() {
    super.updateAddresses()
  }
}
