package click.dobel.shelly.exporter.test

import click.dobel.shelly.exporter.ShellyExporterMetricsConfiguration
import click.dobel.shelly.exporter.metrics.ShellyMetrics
import io.kotest.core.spec.style.FreeSpec
import io.kotest.inspectors.forNone
import io.kotest.inspectors.forOne
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.string.shouldContain
import io.micrometer.core.instrument.DistributionSummary
import io.micrometer.core.instrument.FunctionCounter
import io.micrometer.core.instrument.Gauge
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.server.LocalManagementPort
import org.springframework.context.annotation.Import
import org.springframework.test.context.TestPropertySource
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Import(ShellyExporterMetricsConfiguration::class)
@TestPropertySource(properties = ["management.prometheus.metrics.export.enabled=true"])
class ValueFilteringPrometheusRegistryIntegrationTest(
  @LocalManagementPort
  port: Int,
  registry: MeterRegistry,
) : FreeSpec({

  var counterValue: Double
  var gaugeValue: Double

  val restTemplate = RestTemplate()
  fun getMetrics() = restTemplate
    .getForObject<String?>("http://localhost:$port/prometheus")
    ?.trim()
    ?.lines()
    ?.filterNot { it.startsWith("#") }
    ?: emptyList()

  val expectedMetrics =
    /* counter */ 1 +
    /* gauge */ 1 +
    /* summary */ 3 +
    /* timer */ 3

  beforeEach {
    counterValue = 0.0
    gaugeValue = 0.0
    FunctionCounter.builder("${ShellyMetrics.PREFIX}testcounter", this) { counterValue }.register(registry)
    Gauge.builder("${ShellyMetrics.PREFIX}testgauge", this) { gaugeValue }.register(registry)
    DistributionSummary.builder("${ShellyMetrics.PREFIX}testsummary").register(registry)
    Timer.builder("${ShellyMetrics.PREFIX}testtimer").register(registry)
  }

  afterEach {
    registry.clear()
  }

  "ValueFilteringPrometheusRegistry" - {
    "Should return all values if not invalid" {
      val lines = getMetrics()

      lines.shouldHaveSize(expectedMetrics)
      lines.forOne { it shouldContain "testgauge" }
      lines.forOne { it shouldContain "testcounter" }
    }
    "Should suppress invalid gauges" {
      gaugeValue = Double.NaN
      val lines = getMetrics()

      lines shouldHaveSize (expectedMetrics - 1)
      lines.forNone { it shouldContain "testgauge" }
      lines.forOne { it shouldContain "testcounter" }
    }
    "Should suppress invalid counters" {
      counterValue = Double.NaN
      val lines = getMetrics()

      lines shouldHaveSize (expectedMetrics - 1)
      lines.forOne { it shouldContain "testgauge" }
      lines.forNone { it shouldContain "testcounter" }
    }
    "Should suppress everything" {
      gaugeValue = Double.NaN
      counterValue = Double.NaN
      val lines = getMetrics()

      lines shouldHaveSize (expectedMetrics - 2)
      lines.forNone { it shouldContain "testgauge" }
      lines.forNone { it shouldContain "testcounter" }
    }
  }
})
