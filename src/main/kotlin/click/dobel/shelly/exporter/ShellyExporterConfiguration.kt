package click.dobel.shelly.exporter

import click.dobel.shelly.exporter.client.api.gen1.Settings
import click.dobel.shelly.exporter.client.api.gen1.Shelly
import click.dobel.shelly.exporter.client.api.gen1.Status
import click.dobel.shelly.exporter.client.api.gen2.Gen2ShellyConfig
import click.dobel.shelly.exporter.client.api.gen2.Gen2ShellyDeviceInfo
import click.dobel.shelly.exporter.client.api.gen2.Gen2ShellyStatus
import click.dobel.shelly.exporter.config.ShellyConfigProperties
import click.dobel.shelly.exporter.metrics.ShellyMetrics
import click.dobel.shelly.exporter.metrics.ValueFilteringCollectorRegistry
import com.github.benmanes.caffeine.cache.Caffeine
import io.micrometer.core.instrument.Meter
import io.micrometer.core.instrument.config.MeterFilter
import io.micrometer.core.instrument.config.MeterFilterReply
import io.prometheus.client.CollectorRegistry
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import java.util.concurrent.TimeUnit

@Configuration
@EnableCaching
@EnableScheduling
@EnableConfigurationProperties(ShellyConfigProperties::class)
class ShellyExporterConfiguration {

  companion object {
    // TODO collect automatically
    private val API_CLASSES = listOf(
      Shelly::class,
      Status::class,
      Settings::class,

      Gen2ShellyStatus::class,
      Gen2ShellyDeviceInfo::class,
      Gen2ShellyConfig::class
    )

    const val SCRAPE_FAILURE_VALUE = Double.NaN
  }

  @Bean
  fun cacheNames(): Array<String> = API_CLASSES.map { it.simpleName!! }.toTypedArray()

  // cache is only used to cache shelly http responses during a single metrics call
  // any reasonably short value (shorter than scrape interval) will so
  @Bean
  fun caffeineConfig(): Caffeine<Any, Any> =
    Caffeine
      .newBuilder()
      .expireAfterWrite(10, TimeUnit.SECONDS)

  @Bean
  fun cacheManager(caffeineConfig: Caffeine<Any, Any>): CacheManager {
    val manager = CaffeineCacheManager(*cacheNames())
    manager.setCaffeine(caffeineConfig)
    return manager
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
  fun collectorRegistry(): CollectorRegistry {
    return ValueFilteringCollectorRegistry(SCRAPE_FAILURE_VALUE, true)
  }
}
