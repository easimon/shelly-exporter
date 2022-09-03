package click.dobel.shelly.exporter.logging

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

fun loggerFor(name: String?): Logger = LoggerFactory.getLogger(name ?: "<anonymous>")
fun loggerFor(kotlinClass: KClass<*>): Logger = loggerFor(kotlinClass.qualifiedName)

fun <T : Any> T.logger(): Logger {
  return loggerFor(unwrapCompanion(this::class))
}

private fun unwrapCompanion(kotlinClass: KClass<*>): KClass<*> {
  return if (kotlinClass.isCompanion) {
    kotlinClass.java.enclosingClass.kotlin
  } else {
    kotlinClass
  }
}
