package click.dobel.shelly.exporter.client.api.gen1

import com.fasterxml.jackson.annotation.JsonProperty

data class Shelly(
  @param:JsonProperty("type")
  val type: String,
  @param:JsonProperty("mac")
  val mac: String,
  @param:JsonProperty("auth")
  val auth: Boolean,
  @param:JsonProperty("fw")
  val firmwareVersion: String,
  @param:JsonProperty("longid")
  val hasLongId: Boolean,
)
