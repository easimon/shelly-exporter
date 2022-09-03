package click.dobel.shelly.exporter.client.api

import com.fasterxml.jackson.annotation.JsonProperty

data class Settings(
  @JsonProperty("name")
  val name: String,
  @JsonProperty("lat")
  val lat: Double,
  @JsonProperty("lng")
  val lng: Double,
  @JsonProperty("led_status_disable")
  val ledStatusDisable: Boolean,
  @JsonProperty("fw")
  val firmwareVersion: String,

  @JsonProperty("device")
  val device: Device,
  @JsonProperty("cloud")
  val cloud: Cloud,

  // Shelly Plug only
  @JsonProperty("max_power")
  val maxPower: Double?,
  @JsonProperty("led_power_disable")
  val ledPowerDisable: Boolean?,
) {

  data class Device(
    @JsonProperty("type")
    val type: String,
    @JsonProperty("mac")
    val mac: String,
    @JsonProperty("hostname")
    val hostName: String,
    @JsonProperty("num_outputs")
    val outputCount: Int,
    @JsonProperty("num_meters")
    val meterCount: Int,
  )

  data class Cloud(
    @JsonProperty("enabled")
    val enabled: Boolean,
    @JsonProperty("connected")
    val connected: Boolean
  )
}
