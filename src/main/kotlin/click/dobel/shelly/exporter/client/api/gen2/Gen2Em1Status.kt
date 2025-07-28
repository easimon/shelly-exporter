package click.dobel.shelly.exporter.client.api.gen2

import com.fasterxml.jackson.annotation.JsonProperty

data class Gen2Em1Status(
  @JsonProperty("id")
  val id: Int,

  @JsonProperty("current")
  val current: Double,
  @JsonProperty("voltage")
  val voltage: Double,
  @JsonProperty("act_power")
  val actualPower: Double,
  @JsonProperty("aprt_power")
  val apparentPower: Double,
  @JsonProperty("pf")
  val powerFactor: Double,
  @JsonProperty("freq")
  val frequency: Double,
  @JsonProperty("calibration")
  val calibration: String,

  @JsonProperty("errors")
  val errors: List<String>?,
)
