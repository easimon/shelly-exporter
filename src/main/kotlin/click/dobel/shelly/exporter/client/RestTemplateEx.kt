package click.dobel.shelly.exporter.client

import org.springframework.http.ResponseEntity
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.RestTemplate

inline fun <reified T : Any> RestTemplate.getForEntity(
  url: String,
  vararg uriVariables: Any
): ResponseEntity<T> {
  return getForEntity(url, T::class.java, uriVariables)
}

inline fun <reified T : Any> RestTemplate.getForObject(
  url: String,
  vararg uriVariables: Any
): T {
  return getForObject(url, T::class.java, uriVariables)
    ?: throw ResourceAccessException("I/O error on GET $url: null response.")
}
