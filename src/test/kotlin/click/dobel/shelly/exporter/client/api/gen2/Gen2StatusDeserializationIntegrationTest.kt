package click.dobel.shelly.exporter.client.api.gen2

import click.dobel.shelly.exporter.test.IntegrationTest
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import tools.jackson.databind.ObjectMapper

@IntegrationTest
class Gen2StatusDeserializationIntegrationTest(
  @Autowired objectMapper: ObjectMapper
) : FreeSpec({

  // Shelly Plus Uni with voltmeter add-on, see https://github.com/easimon/shelly-exporter/issues/156
  val plusUniStatus = """
    {
      "ble": {},
      "cloud": { "connected": true },
      "input:0": { "id": 0, "state": null },
      "input:1": { "id": 1, "state": null },
      "input:2": { "id": 2, "counts": null, "freq": null },
      "mqtt": { "connected": true },
      "script:1": { "id": 1, "running": false, "mem_free": 25200 },
      "switch:0": { "id": 0, "source": "init", "output": false },
      "switch:1": { "id": 1, "source": "init", "output": false },
      "sys": {
        "mac": "C4D8D5539154",
        "restart_required": false,
        "time": "19:59",
        "unixtime": 1753289941,
        "last_sync_ts": 1753288970,
        "uptime": 991528,
        "ram_size": 253784,
        "ram_free": 126948,
        "ram_min_free": 107748,
        "fs_size": 393216,
        "fs_free": 106496,
        "cfg_rev": 24,
        "available_updates": { "beta": { "version": "1.7.0-beta3" } },
        "reset_reason": 1,
        "utc_offset": 10800
      },
      "voltmeter:100": { "id": 100, "voltage": 1.39 },
      "wifi": {
        "sta_ip": "10.10.201.122",
        "status": "got ip",
        "ssid": "Moscalu_IoT",
        "rssi": -75
      },
      "ws": { "connected": false }
    }
  """.trimIndent()

  "Deserializing Gen2 Status" - {

    "Should read the voltmeter (Issue #156)" {
      val status = shouldNotThrowAny {
        objectMapper.readValue(plusUniStatus, Gen2ShellyStatus::class.java)
      }

      status.voltmeter100 shouldBe Gen2Voltmeter(id = 100, voltage = 1.39)
      status.voltmeters shouldBe mapOf(100 to Gen2Voltmeter(id = 100, voltage = 1.39))
    }

    "Should succeed with a null voltage" {
      val json = plusUniStatus.replace(""""voltage": 1.39""", """"voltage": null""")

      val status = shouldNotThrowAny {
        objectMapper.readValue(json, Gen2ShellyStatus::class.java)
      }

      status.voltmeters[100]?.voltage shouldBe null
    }

    "Should have no voltmeters when the property is missing" {
      val json = plusUniStatus.replace(""""voltmeter:100": { "id": 100, "voltage": 1.39 },""", "")

      val status = shouldNotThrowAny {
        objectMapper.readValue(json, Gen2ShellyStatus::class.java)
      }

      status.voltmeter100 shouldBe null
      status.voltmeters.shouldBeEmpty()
    }
  }
})
