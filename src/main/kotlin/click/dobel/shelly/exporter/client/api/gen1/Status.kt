package click.dobel.shelly.exporter.client.api.gen1

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

data class Status(
  @param:JsonProperty("relays")
  val relays: List<Relay>,
  @param:JsonProperty("meters")
  val meters: List<Meter>,

  @param:JsonProperty("ram_total")
  val ramTotal: Long,
  @param:JsonProperty("ram_free")
  val ramFree: Long,
  @param:JsonProperty("ram_lwm")
  val ramLowWaterMark: Long,

  @param:JsonProperty("fs_size")
  val fileSystemSize: Long,
  @param:JsonProperty("fs_free")
  val fileSystemFree: Long,
  @param:JsonProperty("uptime")
  val uptime: Long,

  @param:JsonProperty("update")
  val update: Update,

  @param:JsonProperty("wifi_sta")
  val wifiSta: WifiSta,

  @param:JsonProperty("mqtt")
  val mqtt: Mqtt,

  @param:JsonProperty("cloud")
  val cloud: Cloud,

  // Following properties available on Plug S only
  // val temperature: Double?,
  @param:JsonProperty("overtemperature")
  val overTemperature: Boolean?,
  @param:JsonProperty("tmp")
  val temperature: Temperature?,
) {

  data class Relay(
    @param:JsonProperty("ison")
    val isOn: Boolean,
    @param:JsonProperty("has_timer")
    val hasTimer: Boolean,
    @param:JsonProperty("timer_started")
    val timerStarted: Long,
    @param:JsonProperty("timer_duration")
    val timerDuration: Long,
    @param:JsonProperty("timer_remaining")
    val timerRemaining: Long,
    @param:JsonProperty("overpower")
    val overpower: Boolean?,
    @param:JsonProperty("source")
    val source: String,
  )

  data class Meter(
    @param:JsonProperty("power")
    val power: Double,
    @param:JsonProperty("overpower")
    val overpower: Double?,
    @param:JsonProperty("is_valid")
    val isValid: Boolean,
    @param:JsonProperty("timestamp")
    val timestamp: Long?,
    @param:JsonProperty("counters")
    val counters: List<Double> = emptyList(),
    /**
     * total consumption in watt-minutes.
     */
    @param:JsonProperty("total")
    val wattMinutesTotal: Double = 0.0,
  ) {
    @get:JsonIgnore
    val wattHoursTotal get() = wattMinutesTotal / MINUTES_PER_HOUR

    companion object {
      const val MINUTES_PER_HOUR = 60.0
    }
  }

  data class Temperature(
    @param:JsonProperty("tC")
    val celsius: Double,
    @param:JsonProperty("tF")
    val fahrenheit: Double,
    @param:JsonProperty("is_valid")
    val isValid: Boolean,
  )

  data class Update(
    @param:JsonProperty("status")
    val status: String,
    @param:JsonProperty("has_update")
    val hasUpdate: Boolean,
  )

  data class WifiSta(
    @param:JsonProperty("connected")
    val isConnected: Boolean,
    @param:JsonProperty("ssid")
    val ssid: String,
    @param:JsonProperty("ip")
    val ip: String,
    @param:JsonProperty("rssi")
    val rssi: Int,
  )

  data class Mqtt(
    @param:JsonProperty("connected")
    val isConnected: Boolean,
  )

  data class Cloud(
    @param:JsonProperty("enabled")
    val isEnabled: Boolean,
    @param:JsonProperty("connected")
    val isConnected: Boolean,
  )
}
