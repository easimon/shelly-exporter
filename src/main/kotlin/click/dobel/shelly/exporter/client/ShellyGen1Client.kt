package click.dobel.shelly.exporter.client

import click.dobel.shelly.exporter.client.api.gen1.Settings
import click.dobel.shelly.exporter.client.api.gen1.Shelly
import click.dobel.shelly.exporter.client.api.gen1.Status
import click.dobel.shelly.exporter.config.ShellyConfigProperties
import mu.KLogging
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component

@Component
class ShellyGen1Client(
  configProperties: ShellyConfigProperties,
  restTemplateBuilder: RestTemplateBuilder
) : ShellyClient(this) {
  companion object : KLogging()

  @Cacheable("Status", sync = true)
  fun status(address: String) = get<Status>(address, "status")

  @Cacheable("Shelly", sync = true)
  fun shelly(address: String) = get<Shelly>(address, "shelly")

  @Cacheable("Settings", sync = true)
  fun settings(address: String) = get<Settings>(address, "settings")

  override val restTemplate = restTemplateBuilder.createRestTemplate(
    configProperties.auth,
    configProperties.httpParams,
  )
}
