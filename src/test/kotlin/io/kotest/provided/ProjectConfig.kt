package io.kotest.provided

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.extensions.Extension
import io.kotest.extensions.spring.SpringExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode

class ProjectConfig : AbstractProjectConfig() {
  override val extensions: List<Extension>
    get() = super.extensions + listOf(
      SpringExtension(SpringTestLifecycleMode.Test)
    )
}
