package click.dobel.shelly.exporter.client.api.gen2

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

data class Gen2ShellyConfig(
  @param:JsonProperty("cloud")
  val cloud: CloudConfig?,
  @param:JsonProperty("sys")
  val sys: SysConfig?,
  @param:JsonProperty("wifi")
  val wifi: WifiConfig?,
)

data class CloudConfig(
  @param:JsonProperty("enable")
  val isEnabled: Boolean,
  @param:JsonProperty("server")
  val server: String?,
)

data class SysConfig(
  @param:JsonProperty("location")
  val location: ShellyLocation?,
)

data class ShellyLocation(
  @param:JsonProperty("tz")
  val tz: String?,
  @param:JsonProperty("lat")
  val lat: Double?,
  @param:JsonProperty("lon")
  val lng: Double?
)

data class WifiConfig(
  @param:JsonProperty("roam")
  val roam: WifiRoamConfig?,
)

data class WifiRoamConfig(
  @param:JsonProperty("rssi_thr")
  val rssiThreshold: Int,
  @param:JsonProperty("interval")
  val interval: Int,
) {
  @JsonIgnore
  val isEnabled = interval > 0
}
