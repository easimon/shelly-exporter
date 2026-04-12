package click.dobel.shelly.exporter.client.api.gen2

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

data class Gen2EmStatus(
  @param:JsonProperty("id")
  val id: Int,

  @param:JsonProperty("a_current")
  val phaseACurrent: Double?,
  @param:JsonProperty("a_voltage")
  val phaseAVoltage: Double?,
  @param:JsonProperty("a_act_power")
  val phaseAActualPower: Double?,
  @param:JsonProperty("a_aprt_power")
  val phaseAApparentPower: Double?,
  @param:JsonProperty("a_pf")
  val phaseAPowerFactor: Double?,

  @param:JsonProperty("b_current")
  val phaseBCurrent: Double?,
  @param:JsonProperty("b_voltage")
  val phaseBVoltage: Double?,
  @param:JsonProperty("b_act_power")
  val phaseBActualPower: Double?,
  @param:JsonProperty("b_aprt_power")
  val phaseBApparentPower: Double?,
  @param:JsonProperty("b_pf")
  val phaseBPowerFactor: Double?,

  @param:JsonProperty("c_current")
  val phaseCCurrent: Double?,
  @param:JsonProperty("c_voltage")
  val phaseCVoltage: Double?,
  @param:JsonProperty("c_act_power")
  val phaseCActualPower: Double?,
  @param:JsonProperty("c_aprt_power")
  val phaseCApparentPower: Double?,
  @param:JsonProperty("c_pf")
  val phaseCPowerFactor: Double?,

  @param:JsonProperty("n_current")
  val neutralCurrent: Double?,

  @param:JsonProperty("total_current")
  val totalCurrent: Double?,
  @param:JsonProperty("total_act_power")
  val totalActualPower: Double?,
  @param:JsonProperty("total_aprt_power")
  val totalApparentPower: Double?,

  @param:JsonProperty("user_calibrated_phase")
  val userCalibratedPhase: List<String>,

  @param:JsonProperty("errors")
  val errors: List<String>?,
) {
  @JsonIgnore
  val phases = mapOf(
    Gen2PhaseNames.A to PhaseStatus(
      phaseACurrent,
      phaseAVoltage,
      phaseAActualPower,
      phaseAApparentPower,
      phaseAPowerFactor
    ),
    Gen2PhaseNames.B to PhaseStatus(
      phaseBCurrent,
      phaseBVoltage,
      phaseBActualPower,
      phaseBApparentPower,
      phaseBPowerFactor
    ),
    Gen2PhaseNames.C to PhaseStatus(
      phaseCCurrent,
      phaseCVoltage,
      phaseCActualPower,
      phaseCApparentPower,
      phaseCPowerFactor
    ),
    Gen2PhaseNames.TOTAL to PhaseStatus(
      totalCurrent,
      null,
      totalActualPower,
      totalApparentPower,
      null
    ),
  )
}

data class PhaseStatus(
  val current: Double?,
  val voltage: Double?,
  val actualPower: Double?,
  val apparentPower: Double?,
  val powerFactor: Double?,
)
