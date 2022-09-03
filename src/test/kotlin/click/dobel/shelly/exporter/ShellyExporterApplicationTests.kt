package click.dobel.shelly.exporter

import click.dobel.shelly.exporter.config.ShellyConfigProperties
import click.dobel.shelly.exporter.test.IntegrationTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@IntegrationTest
class ShellyExporterApplicationTests(
  @Autowired val configProperties: ShellyConfigProperties
) {

  @Test
  fun contextLoads() {
    print(configProperties)
  }
}
