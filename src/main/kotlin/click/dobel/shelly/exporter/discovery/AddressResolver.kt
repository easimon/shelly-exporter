package click.dobel.shelly.exporter.discovery

import click.dobel.shelly.exporter.logging.logger
import java.net.InetAddress
import java.net.UnknownHostException

fun interface AddressResolver {
  fun resolveToAddresses(name: String): List<InetAddress>
}

object DefaultAddressResolver : AddressResolver {
  private val LOG = logger()

  override fun resolveToAddresses(name: String): List<InetAddress> {
    return try {
      InetAddress.getAllByName(name).asList()
    } catch (ex: UnknownHostException) {
      LOG.warn("Could not resolve '{}'.", name, ex)
      emptyList()
    }
  }
}
