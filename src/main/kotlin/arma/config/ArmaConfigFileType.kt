package arma.config

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

object ArmaConfigFileType : LanguageFileType(ArmaConfigLanguage) {
    override fun getName(): String = "Arma Config"
    @Suppress("DialogTitleCapitalization")
    override fun getDescription(): String = "Arma Config File"
    override fun getDefaultExtension(): String = "cpp"  // not super important, we’ll bind explicitly
    override fun getIcon(): Icon? = null                // optional icon later
}
