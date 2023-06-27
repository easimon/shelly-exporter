package click.dobel.shelly.exporter.client

import click.dobel.shelly.exporter.client.api.gen2.Gen2ShellyConfig
import click.dobel.shelly.exporter.client.api.gen2.Gen2ShellyDeviceInfo
import click.dobel.shelly.exporter.client.api.gen2.Gen2ShellyStatus
import click.dobel.shelly.exporter.config.ShellyConfigProperties
import mu.KLogging
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component


@Component
class ShellyGen2Client(
  configProperties: ShellyConfigProperties,
  restTemplateBuilder: RestTemplateBuilder
) : ShellyClient(this) {

  companion object : KLogging()

  @Cacheable("Gen2ShellyStatus", sync = true)
  fun status(address: String) = get<Gen2ShellyStatus>(address, "rpc/Shelly.GetStatus")

  @Cacheable("Gen2ShellyDeviceInfo", sync = true)
  fun deviceInfo(address: String) = get<Gen2ShellyDeviceInfo>(address, "rpc/Shelly.GetDeviceInfo")

  @Cacheable("Gen2ShellyConfig", sync = true)
  fun config(address: String) = get<Gen2ShellyConfig>(address, "rpc/Shelly.GetConfig")

  override val restTemplate = restTemplateBuilder.createRestTemplate(
    configProperties.gen2auth,
    configProperties.httpParams,
  )
}
