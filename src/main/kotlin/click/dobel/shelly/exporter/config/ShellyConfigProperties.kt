package click.dobel.shelly.exporter.config

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

@ConfigurationProperties(prefix = "shelly")
data class ShellyConfigProperties(
  val auth: Auth,
  val httpParams: HttpParams,
  val devices: Devices,
  val gen2auth: Auth,
  val gen2devices: Devices,
) {

  data class Auth(
    val username: String = "",
    val password: String = "",
    val isEnabled: Boolean = password.isNotBlank()
  )

  data class Devices(
    val discoveryInterval: Duration = 5.minutes.toJavaDuration(),
    val hosts: List<String> = emptyList(),
  )

  data class HttpParams(
    val connectTimeout: Duration = 5.seconds.toJavaDuration(),
    val requestTimeout: Duration = 5.seconds.toJavaDuration(),
    val validationPeriod: Duration = 500.milliseconds.toJavaDuration(),
    val timeToLive: Duration = 5.seconds.toJavaDuration(),
    val maxConnectionsTotal: Int = 200,
    val maxConnectionsPerRoute: Int = 10
  )
}
