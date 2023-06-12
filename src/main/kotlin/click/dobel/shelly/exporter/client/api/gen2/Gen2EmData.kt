package click.dobel.shelly.exporter.client.api.gen2

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

data class Gen2EmData(
  @JsonProperty("id")
  val id: Int,

  @JsonProperty("a_total_act_energy")
  val phaseATotalActualEnergy: Double?,
  @JsonProperty("a_total_act_ret_energy")
  val phaseATotalActualReturnedEnergy: Double?,

  @JsonProperty("b_total_act_energy")
  val phaseBTotalActualEnergy: Double?,
  @JsonProperty("b_total_act_ret_energy")
  val phaseBTotalActualReturnedEnergy: Double?,

  @JsonProperty("c_total_act_energy")
  val phaseCTotalActualEnergy: Double?,
  @JsonProperty("c_total_act_ret_energy")
  val phaseCTotalActualReturnedEnergy: Double?,

  @JsonProperty("total_act")
  val grandTotalActualEnergy: Double?,
  @JsonProperty("total_act_ret")
  val grandTotalActualReturnedEnergy: Double?,
) {
  @JsonIgnore
  val phases = mapOf(
    Gen2PhaseNames.A to PhaseData(phaseATotalActualEnergy, phaseATotalActualReturnedEnergy),
    Gen2PhaseNames.B to PhaseData(phaseBTotalActualEnergy, phaseBTotalActualReturnedEnergy),
    Gen2PhaseNames.C to PhaseData(phaseCTotalActualEnergy, phaseCTotalActualReturnedEnergy),
    Gen2PhaseNames.TOTAL to PhaseData(grandTotalActualEnergy, grandTotalActualReturnedEnergy),
  )
}

data class PhaseData(
  val totalActualEnergy: Double?,
  val totalActualReturnedEnergy: Double?,
)
