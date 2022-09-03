package click.dobel.shelly.exporter.discovery

data class ShellyDevice(
  val mac: String,
  val address: String,
  val name: String,
  val type: String,
  val firmwareVersion: String
)
