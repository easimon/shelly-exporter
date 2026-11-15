package click.dobel.shelly.exporter.client.api.gen2

import click.dobel.shelly.exporter.test.IntegrationTest
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import tools.jackson.databind.ObjectMapper

@IntegrationTest
class Gen2ShellyStatusDeserializationIntegrationTest(
  @Autowired objectMapper: ObjectMapper
) : FreeSpec({

  fun statusJson(addOnTemperatures: String = ""): String {
    val optional = if (addOnTemperatures.isBlank()) "" else "$addOnTemperatures,"
    return """
      {
        "ble": {},
        "cloud": { "connected": true },
        "input:0": { "id": 0, "state": false },
        "mqtt": { "connected": true },
        "switch:0": {
          "id": 0,
          "source": "init",
          "output": false,
          "apower": 0.0,
          "voltage": 233.1,
          "current": 0.000,
          "aenergy": {
            "total": 0.000,
            "by_minute": [0.000, 0.000, 0.000],
            "minute_ts": 1753290120
          },
          "temperature": { "tC": 66.9, "tF": 152.5 }
        },
        "sys": {
          "mac": "CC7B5C82085C",
          "restart_required": false,
          "time": "20:02",
          "unixtime": 1753290147,
          "last_sync_ts": 1753289729,
          "uptime": 527407,
          "ram_size": 253368,
          "ram_free": 128024,
          "ram_min_free": 107452,
          "fs_size": 393216,
          "fs_free": 118784,
          "available_updates": { "beta": { "version": "1.7.0-beta3" } }
        },
        $optional
        "wifi": {
          "sta_ip": "10.10.201.111",
          "status": "got ip",
          "ssid": "Moscalu_IoT",
          "rssi": -61
        },
        "ws": { "connected": false }
      }
    """.trimIndent()
  }

  // https://github.com/easimon/shelly-exporter/issues/157
  val issueTemperatures = """
    "temperature:100": { "id": 100, "tC": 26.1, "tF": 78.9 },
    "temperature:101": { "id": 101, "tC": 26.6, "tF": 79.8 },
    "temperature:102": { "id": 102, "tC": 25.5, "tF": 77.9 },
    "temperature:103": { "id": 103, "tC": 25.6, "tF": 78.0 },
    "temperature:104": { "id": 104, "tC": 42.5, "tF": 108.5 }
  """.trimIndent()

  "Deserializing Gen2 add-on temperature sensors" - {

    "Should read all five sensors (Issue #157)" {
      val status = shouldNotThrowAny {
        objectMapper.readValue(statusJson(issueTemperatures), Gen2ShellyStatus::class.java)
      }

      status.addOnTemperatures.keys shouldBe setOf(100, 101, 102, 103, 104)
      status.addOnTemperatures[100] shouldBe AddOnTemperature(100, 26.1, 78.9, null)
      status.addOnTemperatures[101] shouldBe AddOnTemperature(101, 26.6, 79.8, null)
      status.addOnTemperatures[102] shouldBe AddOnTemperature(102, 25.5, 77.9, null)
      status.addOnTemperatures[103] shouldBe AddOnTemperature(103, 25.6, 78.0, null)
      status.addOnTemperatures[104] shouldBe AddOnTemperature(104, 42.5, 108.5, null)
    }

    "Should keep the device's own temperature unaffected" {
      val status = objectMapper.readValue(statusJson(issueTemperatures), Gen2ShellyStatus::class.java)

      status.switch0?.temperature?.celsius shouldBe 66.9
    }

    "Should succeed with no sensors present" {
      val status = shouldNotThrowAny {
        objectMapper.readValue(statusJson(), Gen2ShellyStatus::class.java)
      }

      status.addOnTemperatures shouldBe emptyMap()
      status.temperature100 shouldBe null
    }

    "Should only return the sensors that are present" {
      val json = statusJson(""""temperature:101": { "id": 101, "tC": 20.0, "tF": 68.0 }""")

      val status = shouldNotThrowAny {
        objectMapper.readValue(json, Gen2ShellyStatus::class.java)
      }

      status.addOnTemperatures.keys shouldBe setOf(101)
    }

    "Should succeed when a sensor reports errors instead of values" {
      val json = statusJson(""""temperature:100": { "id": 100, "errors": ["out_of_range"] }""")

      val status = shouldNotThrowAny {
        objectMapper.readValue(json, Gen2ShellyStatus::class.java)
      }

      status.addOnTemperatures[100] shouldBe AddOnTemperature(100, null, null, listOf("out_of_range"))
    }
  }
})
