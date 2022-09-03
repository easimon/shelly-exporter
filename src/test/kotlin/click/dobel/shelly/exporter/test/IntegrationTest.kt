package click.dobel.shelly.exporter.test

import click.dobel.shelly.exporter.ShellyExporterApplication
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [ShellyExporterApplication::class])
@ConstructorBinding
annotation class IntegrationTest
