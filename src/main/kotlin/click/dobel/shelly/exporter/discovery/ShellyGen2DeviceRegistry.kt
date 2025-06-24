package click.dobel.shelly.exporter.discovery

import click.dobel.shelly.exporter.client.ShellyGen2Client
import click.dobel.shelly.exporter.config.ShellyConfigProperties
import click.dobel.shelly.exporter.metrics.ShellyGen2Metrics
import mu.KLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ShellyGen2DeviceRegistry(
  configProperties: ShellyConfigProperties,
  private val shellyClient: ShellyGen2Client,
  metrics: ShellyGen2Metrics,
  @Autowired(required = false) private val resolver: AddressResolver = DefaultAddressResolver
) : ShellyDeviceRegistry(
  configProperties.gen2devices,
  metrics,
  resolver,
  Companion
) {
  companion object : KLogging()

  override fun addressToDevice(address: String): ShellyDevice? {
    return shellyClient.deviceInfo(address)?.run {
      ShellyDevice(
        mac = mac,
        address = address,
        name = name?.trim() ?: "",
        type = this.model,
        firmwareVersion = firmwareId
      )
    }
  }

  @Scheduled(fixedRateString = "\${shelly.gen2devices.discovery-interval:PT1M}")
  override fun updateAddresses() {
    super.updateAddresses()
  }
}
