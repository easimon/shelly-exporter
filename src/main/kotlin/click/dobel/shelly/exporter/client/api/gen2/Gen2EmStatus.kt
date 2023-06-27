package click.dobel.shelly.exporter.client.api.gen2

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

data class Gen2EmStatus(
  @JsonProperty("id")
  val id: Int,

  @JsonProperty("a_current")
  val phaseACurrent: Double?,
  @JsonProperty("a_voltage")
  val phaseAVoltage: Double?,
  @JsonProperty("a_act_power")
  val phaseAActualPower: Double?,
  @JsonProperty("a_aprt_power")
  val phaseAApparentPower: Double?,
  @JsonProperty("a_pf")
  val phaseAPowerFactor: Double?,

  @JsonProperty("b_current")
  val phaseBCurrent: Double?,
  @JsonProperty("b_voltage")
  val phaseBVoltage: Double?,
  @JsonProperty("b_act_power")
  val phaseBActualPower: Double?,
  @JsonProperty("b_aprt_power")
  val phaseBApparentPower: Double?,
  @JsonProperty("b_pf")
  val phaseBPowerFactor: Double?,

  @JsonProperty("c_current")
  val phaseCCurrent: Double?,
  @JsonProperty("c_voltage")
  val phaseCVoltage: Double?,
  @JsonProperty("c_act_power")
  val phaseCActualPower: Double?,
  @JsonProperty("c_aprt_power")
  val phaseCApparentPower: Double?,
  @JsonProperty("c_pf")
  val phaseCPowerFactor: Double?,

  @JsonProperty("n_current")
  val neutralCurrent: Double?,

  @JsonProperty("total_current")
  val totalCurrent: Double?,
  @JsonProperty("total_act_power")
  val totalActualPower: Double?,
  @JsonProperty("total_aprt_power")
  val totalApparentPower: Double?,

  @JsonProperty("user_calibrated_phase")
  val userCalibratedPhase: List<String>,

  @JsonProperty("errors")
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
