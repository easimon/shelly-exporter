package click.dobel.shelly.exporter.client.api.gen2

import com.fasterxml.jackson.annotation.JsonProperty

data class Gen2Em1Status(
  @param:JsonProperty("id")
  val id: Int,

  @param:JsonProperty("current")
  val current: Double,
  @param:JsonProperty("voltage")
  val voltage: Double,
  @param:JsonProperty("act_power")
  val actualPower: Double,
  @param:JsonProperty("aprt_power")
  val apparentPower: Double,
  @param:JsonProperty("pf")
  val powerFactor: Double,
  @param:JsonProperty("freq")
  val frequency: Double,
  @param:JsonProperty("calibration")
  val calibration: String,

  @param:JsonProperty("errors")
  val errors: List<String>?,
)
