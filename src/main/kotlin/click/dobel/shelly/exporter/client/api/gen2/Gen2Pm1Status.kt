package click.dobel.shelly.exporter.client.api.gen2

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Status of the PM1 component, used by Gen3 pure power-metering devices without relay
 * control (e.g. Shelly PM Mini Gen3).
 */
data class Gen2Pm1Status(
  @param:JsonProperty("id")
  val id: Int,
  @param:JsonProperty("voltage")
  val voltage: Double?,
  @param:JsonProperty("current")
  val current: Double?,
  @param:JsonProperty("apower")
  val power: Double?,
  @param:JsonProperty("aprtpower")
  val apparentPower: Double?,
  @param:JsonProperty("pf")
  val powerFactor: Double?,
  @param:JsonProperty("freq")
  val frequency: Double?,
  @param:JsonProperty("aenergy")
  val energy: Energy?,
  @param:JsonProperty("ret_aenergy")
  val returnedEnergy: Energy?,
) {

  data class Energy(
    val total: Double,
  )
}
