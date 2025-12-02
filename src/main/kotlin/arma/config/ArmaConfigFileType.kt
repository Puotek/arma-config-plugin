package arma.config                          // Same package as language

import com.intellij.openapi.fileTypes.LanguageFileType // Base class for language-based file types
import javax.swing.Icon                      // For file icons in UI

// Object = singleton representing this specific file type
object ArmaConfigFileType : LanguageFileType(ArmaConfigLanguage) {
    // Internal ID of the file type (shown in Settings → Editor → File Types)
    override fun getName(): String = "Arma Config"

    // Human-readable description (UI text)
    @Suppress("DialogTitleCapitalization")
    override fun getDescription(): String = "Arma Config File"

    // Default extension for this file type (used for auto-detection)
    override fun getDefaultExtension(): String = "cpp"  // you can add bindings in plugin.xml too

    // Icon for this file type (null means no custom icon yet)
    override fun getIcon(): Icon? = null                // you can provide an icon later
}
