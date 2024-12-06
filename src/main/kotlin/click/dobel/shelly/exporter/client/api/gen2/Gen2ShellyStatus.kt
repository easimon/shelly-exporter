package click.dobel.shelly.exporter.client.api.gen2

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.EnumSet

@Suppress("LongParameterList")
class Gen2ShellyStatus(
  //@JsonProperty("ble")

  @JsonProperty("cloud")
  val cloud: Cloud,

  @JsonProperty("em:0")
  val emStatus: Gen2EmStatus?,
  @JsonProperty("emdata:0")
  val emData: Gen2EmData?,

  @JsonProperty("input:0")
  val input0: Gen2Input?,
  @JsonProperty("switch:0")
  val switch0: Gen2Switch?,

  //@JsonProperty("eth")
  //@JsonProperty("modbus")

  @JsonProperty("mqtt")
  val mqtt: Mqtt,

  @JsonProperty("sys")
  val sys: SysStatus,

  @JsonProperty("temperature:0")
  val temperature: Temperature,

  @JsonProperty("wifi")
  val wifi: WifiStatus,
) {
  val phaseNames: Set<Gen2PhaseNames> = EnumSet.allOf(Gen2PhaseNames::class.java)
  val phaseStatus = emStatus?.phases
  val phaseData = emData?.phases

  val switches = mapOf(0 to switch0).filterValues { it != null }
}

data class Cloud(
  @JsonProperty("connected")
  val isConnected: Boolean,
)

data class Mqtt(
  @JsonProperty("connected")
  val isConnected: Boolean,
)

data class SysStatus(
  @JsonProperty("mac")
  val mac: String,
  @JsonProperty("restart_required")
  val isRestartRequired: String,
  @JsonProperty("time")
  val time: String,
  @JsonProperty("unixtime")
  val unixTime: Long,
  @JsonProperty("uptime")
  val uptime: Long,

  @JsonProperty("ram_size")
  val ramTotal: Long,
  @JsonProperty("ram_free")
  val ramFree: Long,

  @JsonProperty("fs_size")
  val fsTotal: Long,
  @JsonProperty("fs_free")
  val fsFree: Long,

  @JsonProperty("available_updates")
  val availableUpdates: AvailableUpdates
)

data class AvailableUpdates(
  @JsonProperty("beta")
  val beta: AvailableUpdate?,
  @JsonProperty("stable")
  val stable: AvailableUpdate?,
)

data class AvailableUpdate(
  @JsonProperty("version")
  val version: String,
)

data class Temperature(
  @JsonProperty("tC")
  val celsius: Double,
  @JsonProperty("tF")
  val fahrenheit: Double,
)

data class WifiStatus(
  @JsonProperty("sta_ip")
  val ip: String?,
  @JsonProperty("status")
  val status: String,
  @JsonProperty("ssid")
  val ssid: String?,
  @JsonProperty("rssi")
  val rssi: Int,
)
