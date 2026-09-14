package click.dobel.shelly.exporter.metrics

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class MonotonicCounterCompensatorTest : FreeSpec({

  "MonotonicCounterCompensator" - {
    "first reading" - {
      "should pass through unchanged" {
        val compensator = MonotonicCounterCompensator()
        compensator(1000.0) shouldBe 1000.0
      }
    }

    "NaN values" - {
      "should pass through unchanged, without affecting state" {
        val compensator = MonotonicCounterCompensator()
        compensator(1000.0) shouldBe 1000.0
        compensator(Double.NaN).isNaN() shouldBe true
        compensator(1001.0) shouldBe 1001.0
      }
    }

    "monotonic increases" - {
      "should pass through unchanged" {
        val compensator = MonotonicCounterCompensator()
        compensator(1000.0) shouldBe 1000.0
        compensator(1000.0) shouldBe 1000.0
        compensator(1005.5) shouldBe 1005.5
        compensator(2000.0) shouldBe 2000.0
      }
    }

    "small decrease (flash-persistence artifact)" - {
      "should repeat the last greater value until the raw reading catches back up" {
        val compensator = MonotonicCounterCompensator()
        compensator(1000.0) shouldBe 1000.0
        // device rebooted, resumed a bit below the last reported value
        compensator(995.0) shouldBe 1000.0
        // raw reading is still below the last greater value: keep repeating it
        compensator(998.0) shouldBe 1000.0
        // raw reading caught back up: exposed again from here on
        compensator(1000.0) shouldBe 1000.0
        compensator(1010.0) shouldBe 1010.0
      }

      "should keep repeating the same last greater value across multiple small decreases" {
        val compensator = MonotonicCounterCompensator()
        compensator(1000.0) shouldBe 1000.0
        compensator(995.0) shouldBe 1000.0
        compensator(990.0) shouldBe 1000.0
        compensator(999.0) shouldBe 1000.0
        // only once raw reaches (or exceeds) 1000 again is it exposed
        compensator(1000.0) shouldBe 1000.0
        compensator(1001.0) shouldBe 1001.0
      }
    }

    "large drop close to zero (genuine reset)" - {
      "should pass through unmodified and track the new value going forward" {
        val compensator = MonotonicCounterCompensator()
        compensator(1000.0) shouldBe 1000.0
        compensator(0.2) shouldBe 0.2
        // subsequent increases continue from the reset value
        compensator(5.0) shouldBe 5.0
      }

      "should treat a drop below the configured ratio as genuine even while repeating a prior value" {
        val compensator = MonotonicCounterCompensator()
        compensator(1000.0) shouldBe 1000.0
        compensator(995.0) shouldBe 1000.0 // small decrease, repeats last greater value
        compensator(0.0) shouldBe 0.0 // genuine reset (relative to 1000), tracked value reset
        compensator(1.0) shouldBe 1.0
      }
    }

    "custom reset drop ratio" - {
      "should honor a stricter ratio" {
        // with ratio 0.5, a drop to 60% of the last greater value is still a small decrease
        val compensator = MonotonicCounterCompensator(resetDropRatio = 0.5)
        compensator(1000.0) shouldBe 1000.0
        compensator(600.0) shouldBe 1000.0
        // but a drop to less than 50% of the last greater value (still 1000) is a genuine reset
        compensator(200.0) shouldBe 200.0
      }
    }
  }
})
