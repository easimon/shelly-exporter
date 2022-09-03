package click.dobel.shelly.exporter.discovery

import java.net.Inet4Address

data class ShellyAddress(val value: String) {
  constructor(addr: Inet4Address) : this(addr.hostAddress)
}
