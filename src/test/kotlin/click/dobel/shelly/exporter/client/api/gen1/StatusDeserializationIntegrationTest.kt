package click.dobel.shelly.exporter.client.api.gen1

import click.dobel.shelly.exporter.test.IntegrationTest
import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired

@IntegrationTest
class StatusDeserializationIntegrationTest(
  @Autowired objectMapper: ObjectMapper
) : FreeSpec({

  val message = """
    {
        "wifi_sta": {
            "connected": true,
            "ssid": "my-WLAN",
            "ip": "192.168.0.88",
            "rssi": -72
        },
        "cloud": {
            "enabled": false,
            "connected": false
        },
        "mqtt": {
            "connected": true
        },
        "time": "12:57",
        "unixtime": 1716375473,
        "serial": 1,
        "has_update": false,
        "mac": "123456789012",
        "cfg_changed_cnt": 0,
        "actions_stats": {
            "skipped": 0
        },
        "relays": [
            {
                "ison": false,
                "has_timer": false,
                "timer_started": 0,
                "timer_duration": 0,
                "timer_remaining": 0,
                "source": "mqtt"
            }
        ],
        "meters": [
            {
                "power": 0,
                "is_valid": true
            }
        ],
        "inputs": [
            {
                "input": 0,
                "event": "",
                "event_cnt": 0
            }
        ],
        "ext_sensors": {},
        "ext_temperature": {},
        "ext_humidity": {},
        "update": {
            "status": "idle",
            "has_update": false,
            "new_version": "20230913-112003/v1.14.0-gcb84623",
            "old_version": "20230913-112003/v1.14.0-gcb84623",
            "beta_version": "20231107-162940/v1.14.1-rc1-g0617c15"
        },
        "ram_total": 51688,
        "ram_free": 39728,
        "fs_size": 233681,
        "fs_free": 150349,
        "uptime": 8775365
    }
  """.trimIndent()

  "Deserializing Status" - {

    // https://github.com/easimon/shelly-exporter/issues/103
    "Should succeed when the API does not return counters (Issue #103)" {
      val status = shouldNotThrowAny {
        objectMapper.readValue(message, Status::class.java)
      }

      status.meters.first().counters shouldBe emptyList()
    }
  }
})
