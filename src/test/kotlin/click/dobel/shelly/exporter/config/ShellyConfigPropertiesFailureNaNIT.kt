package click.dobel.shelly.exporter.config

import click.dobel.shelly.exporter.test.IntegrationTest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import org.springframework.test.context.TestPropertySource

@IntegrationTest
@TestPropertySource(
  properties = [
    "shelly.metrics.failure-value=NaN"
  ]
)
class ShellyConfigPropertiesFailureNaNIT(
  configProperties: ShellyConfigProperties
) : FreeSpec({

  "failure-value should be Double.NaN" {
    configProperties.metrics.failureValue shouldBe Double.NaN
  }
})
