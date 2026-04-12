package click.dobel.shelly.exporter.client.api.gen2

import com.fasterxml.jackson.annotation.JsonProperty

data class Gen2Switch(
  @param:JsonProperty("id")
  val id: Int,
  @param:JsonProperty("source")
  val source: String, /* always "init"? */
  @param:JsonProperty("output")
  val outputEnabled: Boolean,
  @param:JsonProperty("apower")
  val power: Double?,
  @param:JsonProperty("voltage")
  val voltage: Double?,
  @param:JsonProperty("current")
  val current: Double?,
  @param:JsonProperty("aenergy")
  val energy: Energy?,
  @param:JsonProperty("ret_aenergy")
  val returnedEnergy: Energy?,
  @param:JsonProperty("pf")
  val powerFactor: Double?,
  @param:JsonProperty("freq")
  val frequency: Double?,
  @param:JsonProperty("temperature")
  val temperature: Temperature?
) {

  data class Energy(
    val total: Double,
  )
}
