package click.dobel.shelly.exporter.client.api.gen2

import com.fasterxml.jackson.annotation.JsonProperty

data class Gen2ShellyDeviceInfo(
  @JsonProperty("name")
  val name: String,
  @JsonProperty("id")
  val id: String,
  @JsonProperty("mac")
  val mac: String,
  @JsonProperty("model")
  val model: String,
  @JsonProperty("gen")
  val gen: Int,
  @JsonProperty("fw_id")
  val firmwareId: String,
  @JsonProperty("ver")
  val version: String,
  @JsonProperty("app")
  val app: String,
  @JsonProperty("auth_en")
  val authEnabled: Boolean,
  @JsonProperty("auth_domain")
  val authDomain: String?,
)
