package click.dobel.shelly.exporter

import click.dobel.shelly.exporter.metrics.ShellyMetrics
import click.dobel.shelly.exporter.metrics.prometheus.ValueFilteringPrometheusRegistry
import io.micrometer.core.instrument.Meter
import io.micrometer.core.instrument.config.MeterFilter
import io.micrometer.core.instrument.config.MeterFilterReply
import io.prometheus.metrics.model.registry.PrometheusRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ShellyExporterMetricsConfiguration {

  companion object {
    const val SCRAPE_FAILURE_VALUE = Double.NaN
  }

  @Bean
  fun meterFilter(): MeterFilter? {
    return object : MeterFilter {
      override fun accept(id: Meter.Id): MeterFilterReply {
        return if (id.name.startsWith(ShellyMetrics.PREFIX)) {
          MeterFilterReply.ACCEPT
        } else {
          MeterFilterReply.DENY
        }
      }
    }
  }

  @Bean
  fun prometheusRegistry(): PrometheusRegistry {
    return ValueFilteringPrometheusRegistry(SCRAPE_FAILURE_VALUE)
  }
}
