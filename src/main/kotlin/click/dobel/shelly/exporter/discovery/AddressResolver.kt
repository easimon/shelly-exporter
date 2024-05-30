package click.dobel.shelly.exporter.discovery

import mu.KLogging
import java.net.InetAddress
import java.net.UnknownHostException

fun interface AddressResolver {
  fun resolveToAddresses(name: String): List<InetAddress>
}

object DefaultAddressResolver : AddressResolver, KLogging() {

  override fun resolveToAddresses(name: String): List<InetAddress> {
    return try {
      InetAddress.getAllByName(name).asList()
    } catch (ex: UnknownHostException) {
      logger.warn { "Could not resolve '${name}': ${ex.message}" }
      logger.trace(ex) { "Stack trace:" }
      emptyList()
    }
  }
}
