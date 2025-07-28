package click.dobel.shelly.exporter.client.api.gen2

import com.fasterxml.jackson.annotation.JsonProperty

data class Gen2Em1Data(
  @JsonProperty("id")
  val id: Int,

  @JsonProperty("total_act_energy")
  val totalActualEnergy: Double,
  @JsonProperty("total_act_ret_energy")
  val totalActualReturnedEnergy: Double,
)
