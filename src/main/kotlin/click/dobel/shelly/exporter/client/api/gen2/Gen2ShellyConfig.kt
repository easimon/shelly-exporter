package click.dobel.shelly.exporter.client.api.gen2

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

data class Gen2ShellyConfig(
  @JsonProperty("cloud")
  val cloud: CloudConfig?,
  @JsonProperty("sys")
  val sys: SysConfig?,
  @JsonProperty("wifi")
  val wifi: WifiConfig?,
)

data class CloudConfig(
  @JsonProperty("enable")
  val isEnabled: Boolean,
  @JsonProperty("server")
  val server: String?,
)

data class SysConfig(
  @JsonProperty("location")
  val location: ShellyLocation?,
)

data class ShellyLocation(
  @JsonProperty("tz")
  val tz: String?,
  @JsonProperty("lat")
  val lat: Double?,
  @JsonProperty("lon")
  val lng: Double?
)

data class WifiConfig(
  @JsonProperty("roam")
  val roam: WifiRoamConfig?,
)

data class WifiRoamConfig(
  @JsonProperty("rssi_thr")
  val rssiThreshold: Int,
  @JsonProperty("interval")
  val interval: Int,
) {
  @JsonIgnore
  val isEnabled = interval > 0
}
