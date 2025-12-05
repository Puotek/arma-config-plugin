package arma.config.formatting.ui

import arma.config.ArmaConfigLanguage
import arma.config.formatting.ArmaConfigCodeStyleSettings
import com.intellij.application.options.CodeStyleAbstractConfigurable
import com.intellij.application.options.CodeStyleAbstractPanel
import com.intellij.application.options.TabbedLanguageCodeStylePanel
import com.intellij.lang.Language
import com.intellij.psi.codeStyle.CodeStyleConfigurable
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CodeStyleSettingsProvider
import com.intellij.psi.codeStyle.CustomCodeStyleSettings


class ArmaConfigCodeStyleSettingsProvider : CodeStyleSettingsProvider() {
    override fun getConfigurableDisplayName(): String = "Arma Config"
    override fun getLanguage(): Language = ArmaConfigLanguage
    override fun getConfigurableId(): String = "editor.codeStyle.ArmaConfig"
    override fun createCustomSettings(settings: CodeStyleSettings): CustomCodeStyleSettings = ArmaConfigCodeStyleSettings(settings)

    override fun createConfigurable(settings: CodeStyleSettings, modelSettings: CodeStyleSettings): CodeStyleConfigurable {
        return object : CodeStyleAbstractConfigurable(settings, modelSettings, configurableDisplayName) {
            override fun createPanel(settings: CodeStyleSettings): CodeStyleAbstractPanel = ArmaConfigCodeStyleMainPanel(currentSettings, settings)
        }
    }

    class ArmaConfigCodeStyleMainPanel(
        currentSettings: CodeStyleSettings,
        settings: CodeStyleSettings
    ) : TabbedLanguageCodeStylePanel(ArmaConfigLanguage, currentSettings, settings)
}
