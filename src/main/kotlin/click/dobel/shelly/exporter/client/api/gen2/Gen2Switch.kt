package click.dobel.shelly.exporter.client.api.gen2

import com.fasterxml.jackson.annotation.JsonProperty

data class Gen2Switch(
  @JsonProperty("id")
  val id: Int,
  @JsonProperty("source")
  val source: String, /* always "init"? */
  @JsonProperty("output")
  val outputEnabled: Boolean,
  @JsonProperty("apower")
  val power: Double?,
  @JsonProperty("voltage")
  val voltage: Double?,
  @JsonProperty("current")
  val current: Double?,
  @JsonProperty("aenergy")
  val energy: Energy?,
  @JsonProperty("ret_aenergy")
  val returnedEnergy: Energy?,
  @JsonProperty("pf")
  val powerFactor: Double?,
  @JsonProperty("freq")
  val frequency: Double?,
  @JsonProperty("temperature")
  val temperature: Temperature?
) {

  data class Energy(
    val total: Double,
  )
}
