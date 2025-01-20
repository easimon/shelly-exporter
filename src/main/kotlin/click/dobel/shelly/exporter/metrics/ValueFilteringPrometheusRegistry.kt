package click.dobel.shelly.exporter.metrics

import io.prometheus.metrics.model.registry.PrometheusRegistry
import io.prometheus.metrics.model.registry.PrometheusScrapeRequest
import io.prometheus.metrics.model.snapshots.CounterSnapshot
import io.prometheus.metrics.model.snapshots.DataPointSnapshot
import io.prometheus.metrics.model.snapshots.GaugeSnapshot
import io.prometheus.metrics.model.snapshots.MetricMetadata
import io.prometheus.metrics.model.snapshots.MetricSnapshot
import io.prometheus.metrics.model.snapshots.MetricSnapshots
import mu.KLogging
import java.util.function.Predicate

class ValueFilteringPrometheusRegistry(
  invalidValue: Double,
) : PrometheusRegistry() {

  private val doubleValidator = DoubleValidator(invalidValue)

  companion object : KLogging()

  private fun Double.isValid(metricName: String): Boolean {
    return doubleValidator.isValid(this)
      .also { valid ->
        if (!valid) {
          logger.debug { "Suppressing invalid reading $this of metric $metricName." }
        }
      }
  }

  private fun metricName(metadata: MetricMetadata, dataPointSnapshot: DataPointSnapshot): String {
    val labels = dataPointSnapshot.labels.joinToString(prefix = "{", postfix = "}") { "${it.name}='${it.value}'" }
    return "${metadata.name}${labels}"
  }

  override fun scrape(): MetricSnapshots {
    return super.scrape().removeInvalidDataPoints()
  }

  override fun scrape(includedNames: Predicate<String>?): MetricSnapshots {
    return super.scrape(includedNames).removeInvalidDataPoints()
  }

  override fun scrape(scrapeRequest: PrometheusScrapeRequest?): MetricSnapshots {
    return super.scrape(scrapeRequest).removeInvalidDataPoints()
  }

  override fun scrape(includedNames: Predicate<String>?, scrapeRequest: PrometheusScrapeRequest?): MetricSnapshots {
    return super.scrape(includedNames, scrapeRequest).removeInvalidDataPoints()
  }

  private fun MetricSnapshots.removeInvalidDataPoints(): MetricSnapshots {
    val results = MetricSnapshots.builder()

    this
      .map { metricSnapshot -> metricSnapshot.filtered() }
      .filter { it.dataPoints.isNotEmpty() }
      .forEach(results::metricSnapshot)

    return results.build()
  }

  private fun MetricSnapshot.filtered() = when (this) {
    is CounterSnapshot -> filtered()
    is GaugeSnapshot -> filtered()
    else -> this
  }

  private fun CounterSnapshot.filtered() = CounterSnapshot(
    this.metadata,
    this.dataPoints.filter { dataPointSnapshot: CounterSnapshot.CounterDataPointSnapshot ->
      dataPointSnapshot.value.isValid(metricName(this.metadata, dataPointSnapshot))
    }
  )

  private fun GaugeSnapshot.filtered() = GaugeSnapshot(
    this.metadata,
    this.dataPoints.filter { dataPointSnapshot: GaugeSnapshot.GaugeDataPointSnapshot ->
      dataPointSnapshot.value.isValid(metricName(this.metadata, dataPointSnapshot))
    }
  )
}
