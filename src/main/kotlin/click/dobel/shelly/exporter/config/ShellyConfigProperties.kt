package click.dobel.shelly.exporter.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration

@ConstructorBinding
@ConfigurationProperties(prefix = "shelly")
data class ShellyConfigProperties(
  val auth: Auth,
  val devices: Devices
) {

  data class Auth(
    val username: String = "",
    val password: String = ""
  )

  data class Devices(
    val discoveryInterval: Duration = 5.minutes.toJavaDuration(),
    val connectTimeout: Duration = 300.milliseconds.toJavaDuration(),
    val requestTimeout: Duration = 300.milliseconds.toJavaDuration(),
    val hosts: List<String> = emptyList(),
  )

  val hasAuth get() = auth.username.isNotBlank() || auth.password.isNotBlank()
}
