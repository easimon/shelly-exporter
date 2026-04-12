package click.dobel.shelly.exporter.client.api.gen2

import com.fasterxml.jackson.annotation.JsonProperty

data class Gen2ShellyDeviceInfo(
  @param:JsonProperty("name")
  val name: String?,
  @param:JsonProperty("id")
  val id: String,
  @param:JsonProperty("mac")
  val mac: String,
  @param:JsonProperty("model")
  val model: String,
  @param:JsonProperty("gen")
  val gen: Int,
  @param:JsonProperty("fw_id")
  val firmwareId: String,
  @param:JsonProperty("ver")
  val version: String,
  @param:JsonProperty("app")
  val app: String,
  @param:JsonProperty("auth_en")
  val authEnabled: Boolean,
  @param:JsonProperty("auth_domain")
  val authDomain: String?,
)
