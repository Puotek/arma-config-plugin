package arma.config.formatting

import arma.config.ArmaConfigLanguage
import com.intellij.application.options.IndentOptionsEditor
import com.intellij.application.options.SmartIndentOptionsEditor
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable
import com.intellij.psi.codeStyle.CustomCodeStyleSettings
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider

class ArmaConfigLanguageCodeStyleSettingsProvider : LanguageCodeStyleSettingsProvider() {

    val settingsClass = ArmaConfigCodeStyleSettings::class.java

    override fun getLanguage() = ArmaConfigLanguage
    override fun createCustomSettings(settings: CodeStyleSettings): CustomCodeStyleSettings = ArmaConfigCodeStyleSettings(settings)
    override fun getIndentOptionsEditor(): IndentOptionsEditor = SmartIndentOptionsEditor(this)
    override fun getCodeSample(settingsType: SettingsType): String = """
        class Example {
            arrayInline[] = {1, 2, 3};
            arrayMultiline[] = {
                1,
                2,
                3
            };
        };
    """.trimIndent()

    override fun customizeSettings(consumer: CodeStyleSettingsCustomizable, settingsType: SettingsType) {
        when (settingsType) {
            SettingsType.INDENT_SETTINGS -> customizeIndentSettings(consumer)
            SettingsType.SPACING_SETTINGS -> customizeSpacingSettings(consumer)
            SettingsType.WRAPPING_AND_BRACES_SETTINGS -> customizeWrappingSettings(consumer)
            SettingsType.BLANK_LINES_SETTINGS -> customizeBlankLinesSettings(consumer)
            else -> Unit
        }
    }

    private fun customizeIndentSettings(consumer: CodeStyleSettingsCustomizable) {
        consumer.showStandardOptions(
            "INDENT_SIZE",
            "USE_TAB_CHARACTER",
            "KEEP_INDENTS_ON_EMPTY_LINES"
        )
        consumer.showCustomOption(
            settingsClass,
            "KEEP_EMPTY_ARRAY_ON_ONE_LINE",
            "Keep empty array on one line",
            "Arrays",
        )
    }

    private fun customizeSpacingSettings(consumer: CodeStyleSettingsCustomizable) {
        consumer.showCustomOption(
            settingsClass,
            "KEEP_EMPTY_ARRAY_ON_ONE_LINE",
            "Keep empty array on one line",
            "Arrays",
        )
    }

    private fun customizeWrappingSettings(consumer: CodeStyleSettingsCustomizable) {
        val groupName = "Arrays"
        consumer.showCustomOption(
            settingsClass,
            "WRAP_ARRAYS",
            groupName,
            null,
            ArmaConfigCodeStyleSettings.ArrayLayoutMode.labels,
            ArmaConfigCodeStyleSettings.ArrayLayoutMode.ordinals
        )
/*
        consumer.showCustomOption(
            settingsClass,
            "WRAP_ARRAYS",
            "Arrays",
            "Arrays",
            ArmaConfigCodeStyleSettings.ArrayLayoutMode.labels,
            ArmaConfigCodeStyleSettings.ArrayLayoutMode.ordinals
        )
*/
        consumer.showCustomOption(
            settingsClass,
            "KEEP_EMPTY_ARRAY_ON_ONE_LINE",
            "Keep empty array on one line",
            groupName,
        )

    }

    private fun customizeBlankLinesSettings(consumer: CodeStyleSettingsCustomizable) {
        consumer.showCustomOption( //fixme this is not showing up in options
            settingsClass,
            "BLANK_LINES_AROUND_CLASS",
            "Blank lines around class",
            "Blank lines"
        )
        consumer.showCustomOption(
            settingsClass,
            "KEEP_EMPTY_ARRAY_ON_ONE_LINE",
            "Keep empty array on one line",
            CodeStyleSettingsCustomizable.BLANK_LINES //deprecated but works
        )

    }
}
