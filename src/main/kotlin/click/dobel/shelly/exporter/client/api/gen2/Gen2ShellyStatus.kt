package click.dobel.shelly.exporter.client.api.gen2

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.EnumSet

@Suppress("LongParameterList")
class Gen2ShellyStatus(
  //@param:JsonProperty("ble")

  @param:JsonProperty("cloud")
  val cloud: Cloud?,

  @param:JsonProperty("em:0")
  val emStatus: Gen2EmStatus?,
  @param:JsonProperty("emdata:0")
  val emData: Gen2EmData?,

  @param:JsonProperty("em1:0")
  val em1Status0: Gen2Em1Status?,
  @param:JsonProperty("em1data:0")
  val em1Data0: Gen2Em1Data?,

  @param:JsonProperty("em1:1")
  val em1Status1: Gen2Em1Status?,
  @param:JsonProperty("em1data:1")
  val em1Data1: Gen2Em1Data?,

  @param:JsonProperty("input:0")
  val input0: Gen2Input?,
  @param:JsonProperty("switch:0")
  val switch0: Gen2Switch?,

  //@param:JsonProperty("eth")
  //@param:JsonProperty("modbus")

  @param:JsonProperty("mqtt")
  val mqtt: Mqtt?,

  @param:JsonProperty("sys")
  val sys: SysStatus?,

  @param:JsonProperty("temperature:0")
  val temperature: Temperature?,

  @param:JsonProperty("wifi")
  val wifi: WifiStatus?,
) {
  val phaseNames: Set<Gen2PhaseNames> = EnumSet.allOf(Gen2PhaseNames::class.java)
  val phaseStatus = emStatus?.phases
  val phaseData = emData?.phases

  val switches = mapOf(0 to switch0).filterValues { it != null }
  val em1Statuses = mapOf("em0" to em1Status0, "em1" to em1Status1).filterValues { it != null }
  val em1Datas = mapOf("em0" to em1Data0, "em1" to em1Data1).filterValues { it != null }
}

data class Cloud(
  @param:JsonProperty("connected")
  val isConnected: Boolean,
)

data class Mqtt(
  @param:JsonProperty("connected")
  val isConnected: Boolean,
)

data class SysStatus(
  @param:JsonProperty("mac")
  val mac: String,
  @param:JsonProperty("restart_required")
  val isRestartRequired: String,
  @param:JsonProperty("time")
  val time: String,
  @param:JsonProperty("unixtime")
  val unixTime: Long,
  @param:JsonProperty("uptime")
  val uptime: Long,

  @param:JsonProperty("ram_size")
  val ramTotal: Long,
  @param:JsonProperty("ram_min_free")
  val ramMinFree: Long?,
  @param:JsonProperty("ram_free")
  val ramFree: Long,

  @param:JsonProperty("fs_size")
  val fsTotal: Long,
  @param:JsonProperty("fs_free")
  val fsFree: Long,

  @param:JsonProperty("available_updates")
  val availableUpdates: AvailableUpdates
)

data class AvailableUpdates(
  @param:JsonProperty("beta")
  val beta: AvailableUpdate?,
  @param:JsonProperty("stable")
  val stable: AvailableUpdate?,
)

data class AvailableUpdate(
  @param:JsonProperty("version")
  val version: String,
)

data class Temperature(
  @param:JsonProperty("tC")
  val celsius: Double,
  @param:JsonProperty("tF")
  val fahrenheit: Double,
)

data class WifiStatus(
  @param:JsonProperty("sta_ip")
  val ip: String?,
  @param:JsonProperty("status")
  val status: String,
  @param:JsonProperty("ssid")
  val ssid: String?,
  @param:JsonProperty("rssi")
  val rssi: Int,
)
