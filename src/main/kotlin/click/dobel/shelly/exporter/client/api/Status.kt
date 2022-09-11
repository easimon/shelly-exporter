package click.dobel.shelly.exporter.client.api

import com.fasterxml.jackson.annotation.JsonProperty

data class Status(
  @JsonProperty("relays")
  val relays: List<Relay>,
  @JsonProperty("meters")
  val meters: List<Meter>,


  @JsonProperty("ram_total")
  val ramTotal: Long,
  @JsonProperty("ram_free")
  val ramFree: Long,
  @JsonProperty("ram_lwm")
  val ramLowWaterMark: Long,

  @JsonProperty("fs_size")
  val fileSystemSize: Long,
  @JsonProperty("fs_free")
  val fileSystemFree: Long,
  @JsonProperty("uptime")
  val uptime: Long,

  @JsonProperty("update")
  val update: Update,

  // Following properties available on Plug S only
  // val temperature: Double?,
  @JsonProperty("overtemperature")
  val overTemperature: Boolean?,
  @JsonProperty("tmp")
  val temperature: Temperature?
) {

  data class Relay(
    @JsonProperty("ison")
    val isOn: Boolean,
    @JsonProperty("has_timer")
    val hasTimer: Boolean,
    @JsonProperty("timer_started")
    val timerStarted: Long,
    @JsonProperty("timer_duration")
    val timerDuration: Long,
    @JsonProperty("timer_remaining")
    val timerRemaining: Long,
    @JsonProperty("overpower")
    val overpower: Boolean,
    @JsonProperty("source")
    val source: String,
  )

  data class Meter(
    @JsonProperty("power")
    val power: Double,
    @JsonProperty("overpower")
    val overpower: Double,
    @JsonProperty("is_valid")
    val isValid: Boolean,
    @JsonProperty("timestamp")
    val timestamp: Long,
    @JsonProperty("counters")
    val counters: List<Double>,
    @JsonProperty("total")
    val total: Double,
  )

  data class Temperature(
    @JsonProperty("tC")
    val celsius: Double,
    @JsonProperty("tF")
    val fahrenheit: Double,
    @JsonProperty("is_valid")
    val isValid: Boolean
  )

  data class Update(
    @JsonProperty("status")
    val status: String,
    @JsonProperty("has_update")
    val hasUpdate: Boolean,
  )
}
