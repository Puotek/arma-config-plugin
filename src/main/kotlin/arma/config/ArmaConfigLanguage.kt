package arma.config          // Package for all Arma config plugin classes

import com.intellij.lang.Language // Base IntelliJ class representing a language

// Object = singleton, one instance of ArmaConfigLanguage in the JVM
@Suppress("JavaIoSerializableObjectMustHaveReadResolve")
object ArmaConfigLanguage : Language("ArmaConfig") {
    // Display name shown in UI (e.g. in Settings, File Types, etc.)
    override fun getDisplayName(): String = "Arma Config"
}
