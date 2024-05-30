package click.dobel.shelly.exporter.client

import click.dobel.shelly.exporter.config.ShellyConfigProperties
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials
import org.apache.hc.client5.http.config.ConnectionConfig
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder
import org.apache.hc.core5.http.io.SocketConfig
import org.apache.hc.core5.pool.PoolConcurrencyPolicy
import org.apache.hc.core5.util.TimeValue
import org.apache.hc.core5.util.Timeout
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestTemplate

fun url(
  address: String,
  path: String
): String = "http://${address}${slash(path)}"

private fun slash(
  path: String
): String = if (path.startsWith("/")) path else "/$path"

private fun <T> T.runIf(condition: Boolean, block: T.() -> T): T {
  return if (condition)
    block()
  else
    this
}

fun RestTemplateBuilder.createRestTemplate(
  auth: ShellyConfigProperties.Auth,
  httpParams: ShellyConfigProperties.HttpParams,
): RestTemplate {
  val httpClient = httpClient(
    auth,
    httpParams,
  )

  return this
    .requestFactory { -> HttpComponentsClientHttpRequestFactory(httpClient) }
    .build()
}

private fun httpClient(
  auth: ShellyConfigProperties.Auth,
  httpParams: ShellyConfigProperties.HttpParams,
) = HttpClients
  .custom()
  .runIf(auth.isEnabled) {
    val credentials = UsernamePasswordCredentials(
      auth.username,
      auth.password.toCharArray()
    )
    setDefaultCredentialsProvider { _, _ -> credentials }
  }
  .disableCookieManagement()
  .disableRedirectHandling()
  .disableAuthCaching()
  .disableConnectionState()
  .setConnectionReuseStrategy { _, _, _ -> false }
  .setConnectionManager(
    PoolingHttpClientConnectionManagerBuilder.create()
      .setMaxConnTotal(httpParams.maxConnectionsTotal)
      .setMaxConnPerRoute(httpParams.maxConnectionsPerRoute)
      .setPoolConcurrencyPolicy(PoolConcurrencyPolicy.LAX)
      .setDefaultSocketConfig(
        SocketConfig.custom()
          .setSoTimeout(Timeout.of(httpParams.requestTimeout))
          .build()
      )
      .setDefaultConnectionConfig(
        ConnectionConfig.custom()
          .setConnectTimeout(Timeout.of(httpParams.connectTimeout))
          .setSocketTimeout(Timeout.of(httpParams.requestTimeout))
          .setTimeToLive(TimeValue.of(httpParams.timeToLive))
          .setValidateAfterInactivity(Timeout.of(httpParams.validationPeriod))
          .build()
      )
      .build()
  )
  .build()
