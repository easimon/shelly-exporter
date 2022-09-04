package click.dobel.shelly.exporter.client

import click.dobel.shelly.exporter.client.api.Settings
import click.dobel.shelly.exporter.client.api.Shelly
import click.dobel.shelly.exporter.client.api.Status
import click.dobel.shelly.exporter.config.ShellyConfigProperties
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class ShellyClient(
  configProperties: ShellyConfigProperties,
  restTemplateBuilder: RestTemplateBuilder
) {

  @Cacheable("Status", sync = true)
  fun status(address: String) = get<Status>(address, "status")

  @Cacheable("Shelly", sync = true)
  fun shelly(address: String) = get<Shelly>(address, "shelly")

  @Cacheable("Settings", sync = true)
  fun settings(address: String) = get<Settings>(address, "settings")

  private inline fun <reified T : Any> get(
    address: String,
    path: String
  ): T {
    return restTemplate.getForObject(url(address, path))
  }

  private fun url(
    address: String,
    path: String
  ): String = "http://${address}${slash(path)}"

  private fun slash(
    path: String
  ): String = if (path.startsWith("/")) path else "/$path"

  private val restTemplate: RestTemplate = restTemplateBuilder
    .setConnectTimeout(configProperties.devices.connectTimeout)
    .setReadTimeout(configProperties.devices.requestTimeout)
    .runIf(configProperties.hasAuth) {
      basicAuthentication(
        configProperties.auth.username,
        configProperties.auth.password
      )
    }
    .build()

  private inline fun <T> T.runIf(condition: Boolean, block: T.() -> T): T {
    return if (condition)
      block()
    else
      this
  }
}
