package click.dobel.shelly.exporter.config

import click.dobel.shelly.exporter.test.IntegrationTest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import org.springframework.test.context.TestPropertySource

@IntegrationTest
@TestPropertySource(
  properties = [
    "shelly.metrics.failure-value=0"
  ]
)
class ShellyConfigPropertiesFailureZeroIT(
  configProperties: ShellyConfigProperties
) : FreeSpec({

  "failure-value should be 0.0" {
    configProperties.metrics.failureValue shouldBe 0.0
  }
})
