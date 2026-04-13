package click.dobel.shelly.exporter.client.api.gen2

import com.fasterxml.jackson.annotation.JsonProperty

data class Gen2Em1Data(
  @param:JsonProperty("id")
  val id: Int,

  @param:JsonProperty("total_act_energy")
  val totalActualEnergy: Double,
  @param:JsonProperty("total_act_ret_energy")
  val totalActualReturnedEnergy: Double,
)
