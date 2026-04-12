package click.dobel.shelly.exporter.client.api.gen1

import com.fasterxml.jackson.annotation.JsonProperty

data class Settings(
  @param:JsonProperty("name")
  val name: String,
  @param:JsonProperty("lat")
  val lat: Double,
  @param:JsonProperty("lng")
  val lng: Double,
  @param:JsonProperty("led_status_disable")
  val isStatusLedDisabled: Boolean,
  @param:JsonProperty("fw")
  val firmwareVersion: String,

  @param:JsonProperty("device")
  val device: Device,

  @param:JsonProperty("ap_roaming")
  val apRoaming: ApRoaming,

  // Shelly Plug only
  @param:JsonProperty("max_power")
  val maxPower: Double?,
  @param:JsonProperty("led_power_disable")
  val isPowerLedDisabled: Boolean?,
) {

  data class Device(
    @param:JsonProperty("type")
    val type: String,
    @param:JsonProperty("mac")
    val mac: String,
    @param:JsonProperty("hostname")
    val hostName: String,
    @param:JsonProperty("num_outputs")
    val outputCount: Int,
    @param:JsonProperty("num_meters")
    val meterCount: Int,
  )

  data class ApRoaming(
    @param:JsonProperty("enabled")
    val isEnabled: Boolean,
    @param:JsonProperty("threshold")
    val threshold: Int,
  )
}
