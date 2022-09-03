package click.dobel.shelly.exporter.client.api

import com.fasterxml.jackson.annotation.JsonProperty

data class Shelly(
  @JsonProperty("type")
  val type: String,
  @JsonProperty("mac")
  val mac: String,
  @JsonProperty("auth")
  val auth: Boolean,
  @JsonProperty("fw")
  val firmwareVersion: String,
  @JsonProperty("longid")
  val longId: Boolean,
)
