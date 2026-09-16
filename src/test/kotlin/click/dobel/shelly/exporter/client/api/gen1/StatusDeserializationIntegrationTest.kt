package click.dobel.shelly.exporter.client.api.gen1

import click.dobel.shelly.exporter.test.IntegrationTest
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import tools.jackson.databind.ObjectMapper

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

  fun statusJson(meters: String? = null, emeters: String? = null): String {
    val optionalFields = listOfNotNull(
      meters?.let { """"meters": $it""" },
      emeters?.let { """"emeters": $it""" },
    ).joinToString("") { ",\n        $it" }

    return """
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
          "update": {
              "status": "idle",
              "has_update": false
          },
          "ram_total": 51688,
          "ram_free": 39728,
          "fs_size": 233681,
          "fs_free": 150349,
          "uptime": 8775365
          $optionalFields
      }
    """.trimIndent()
  }

  "Deserializing Status" - {

    // https://github.com/easimon/shelly-exporter/issues/103
    "Should succeed when the API does not return counters (Issue #103)" {
      val status = shouldNotThrowAny {
        objectMapper.readValue(message, Status::class.java)
      }

      status.meters.first().counters shouldBe emptyList()
    }

    "Should succeed when both meters and emeters are missing" {
      val status = shouldNotThrowAny {
        objectMapper.readValue(statusJson(), Status::class.java)
      }

      status.meters shouldBe emptyList()
      status.emeters shouldBe emptyList()
    }

    "Should succeed when meters is present and emeters is missing" {
      val json = statusJson(meters = """[{"power": 1.5, "is_valid": true}]""")

      val status = shouldNotThrowAny {
        objectMapper.readValue(json, Status::class.java)
      }

      status.meters shouldBe listOf(
        Status.Meter(power = 1.5, overpower = null, isValid = true, timestamp = null)
      )
      status.emeters shouldBe emptyList()
    }

    "Should succeed when emeters is present and meters is missing" {
      val json = statusJson(
        emeters = """[{"power": 2.5, "is_valid": true, "total": 10.0, "total_returned": 0.0}]"""
      )

      val status = shouldNotThrowAny {
        objectMapper.readValue(json, Status::class.java)
      }

      status.meters shouldBe emptyList()
      status.emeters shouldBe listOf(
        Status.Emeter(
          power = 2.5,
          reactive = null,
          powerFactor = null,
          current = null,
          voltage = null,
          isValid = true,
          wattMinutesTotal = 10.0,
          wattMinutesTotalReturned = 0.0
        )
      )
    }

    "Should succeed when both meters and emeters are present" {
      val json = statusJson(
        meters = """[{"power": 1.5, "is_valid": true}]""",
        emeters = """[{"power": 2.5, "is_valid": true, "total": 10.0, "total_returned": 0.0}]"""
      )

      val status = shouldNotThrowAny {
        objectMapper.readValue(json, Status::class.java)
      }

      status.meters.size shouldBe 1
      status.emeters.size shouldBe 1
    }
  }
})
