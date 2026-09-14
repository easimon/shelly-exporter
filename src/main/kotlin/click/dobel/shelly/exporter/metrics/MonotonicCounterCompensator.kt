package click.dobel.shelly.exporter.metrics

/**
 * Compensates for small, non-monotonic decreases reported by Shelly energy meters.
 *
 * Shelly devices don't persist every counter increment to flash (to protect it from wear).
 * After a reboot (power loss, firmware upgrade, ...), a counter can resume from a value
 * slightly *smaller* than what was last reported, even though it never really decreased.
 * Prometheus interprets any decrease of a counter as a full reset to zero, which corrupts
 * `rate()`/`increase()` calculations with large spurious spikes.
 *
 * This class keeps track of the highest raw value seen so far ([lastReportedValue]). As long
 * as a new reading is smaller than that value, but not small enough to be a genuine reset, the
 * last reported value is repeated instead of the (temporarily lower) raw reading, so the exposed
 * value never decreases. Once the raw reading catches back up (becomes greater than or equal to
 * [lastReportedValue]), it is exposed again. Only a large relative drop (the source counter
 * genuinely resetting to, or close to, zero) is passed through unmodified, since Prometheus
 * already handles a true reset-to-zero correctly.
 *
 * One instance of this class must be used per logical counter (e.g. per device/channel/metric)
 * to keep its state properly scoped.
 */
class MonotonicCounterCompensator(
  private val resetDropRatio: Double = DEFAULT_RESET_DROP_RATIO
) {
  companion object {
    /**
     * If a new reading drops below (lastGreaterValue * resetDropRatio), it is considered a
     * genuine reset of the source counter (e.g. to 0), rather than a small flash-persistence
     * artifact. Default: a drop of more than 90% (i.e. the new value is less than 10% of the
     * last greater value).
     */
    const val DEFAULT_RESET_DROP_RATIO = 0.1
  }

  private var lastReportedValue: Double = 0.0

  operator fun invoke(raw: Double): Double {
    if (raw.isNaN()) {
      return raw
    }

    val last = lastReportedValue
    if (raw >= last) {
      lastReportedValue = raw
      return raw
    }

    // raw < last: the source counter decreased.
    val isReset = raw < last * resetDropRatio
    return if (isReset) {
      lastReportedValue = raw
      raw
    } else {
      // small decrease: repeat the last reported value until raw catches back up.
      last
    }
  }
}
