package click.dobel.shelly.exporter.client.api.gen2

enum class Gen2PhaseNames(val metricName: String) {
  A("a"),
  B("b"),
  C("c"),
  TOTAL("total");

  override fun toString() = metricName
}
