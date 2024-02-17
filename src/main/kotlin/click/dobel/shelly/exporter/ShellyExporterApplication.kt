package click.dobel.shelly.exporter

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ShellyExporterApplication

fun main(args: Array<String>) {
  @Suppress("SpreadOperator")
  runApplication<ShellyExporterApplication>(*args)
}
