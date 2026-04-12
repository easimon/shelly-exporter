package click.dobel.shelly.exporter.client

import io.github.oshai.kotlinlogging.KLogger
import org.springframework.web.client.RestTemplate
import  org.springframework.web.client.getForObject

abstract class ShellyClient(
  protected val logger: KLogger
) {

  companion object {
    const val RETRIES = 3
  }

  protected abstract val restTemplate: RestTemplate

  protected inline fun <reified T : Any> get(
    address: String,
    path: String
  ): T? {
    val url = url(address, path)
    return runCatching {
      retry(RETRIES) {
        restTemplate.getForObject<T>(url)
      }
    }.getOrElse { ex ->
      this@ShellyClient.logger.warn { "GET ${url}: HTTP Request failure: ${ex.message}" }
      this@ShellyClient.logger.trace(ex) { "Stack trace:" }
      null
    }
  }

  inline fun <T> retry(
    retries: Int = 1,
    call: () -> T
  ): T? {
    retryLoop@ for (i in 0..retries) {
      val result = runCatching(call)
      if (result.isFailure && i < retries) {
        continue@retryLoop
      }
      return result.getOrThrow()
    }
    error("Retries finished unexpectedly. Retries < 1?.")
  }
}
