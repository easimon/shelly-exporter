package click.dobel.shelly.exporter.client

import mu.KLoggable
import org.springframework.web.client.RestTemplate

abstract class ShellyClient(
  loggable: KLoggable
) {

  companion object {
    const val RETRIES = 3
  }

  protected abstract val restTemplate: RestTemplate
  protected val logger = loggable.logger

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
      logger.warn { "GET ${url}: HTTP Request failure: ${ex.message}" }
      logger.trace(ex) { "Stack trace:" }
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
