package click.dobel.shelly.exporter.client.api.gen2

import com.fasterxml.jackson.annotation.JsonProperty

data class Gen2Voltmeter(
  @param:JsonProperty("id")
  val id: Int,
  /** Voltage in volts, null if no valid value could be obtained. */
  @param:JsonProperty("voltage")
  val voltage: Double?,
)
