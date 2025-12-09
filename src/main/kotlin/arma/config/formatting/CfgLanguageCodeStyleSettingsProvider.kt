package arma.config.formatting

import arma.config.CfgLanguage
import com.intellij.application.options.IndentOptionsEditor
import com.intellij.application.options.SmartIndentOptionsEditor
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable
import com.intellij.psi.codeStyle.CustomCodeStyleSettings
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider

class CfgLanguageCodeStyleSettingsProvider : LanguageCodeStyleSettingsProvider() {

    private val settingsClass = CfgCodeStyleSettings::class.java

    override fun getLanguage() = CfgLanguage
    override fun createCustomSettings(settings: CodeStyleSettings): CustomCodeStyleSettings = CfgCodeStyleSettings(settings)
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
/*
        consumer.showCustomOption(
            settingsClass,
            "KEEP_EMPTY_ARRAY_ON_ONE_LINE",
            "Keep empty array on one line",
            "Arrays",
        )
*/
    }

    private fun customizeSpacingSettings(consumer: CodeStyleSettingsCustomizable) {
        consumer.showCustomOption(
            settingsClass,
            "ARRAYS_SPACE_EQUALS_BEFORE",
            "Space before `=`",
            groupArrays,
        )
        consumer.showCustomOption(
            settingsClass,
            "ARRAYS_SPACE_EQUALS_AFTER",
            "Space after `=`",
            groupArrays,
        )
    }

    private fun customizeWrappingSettings(consumer: CodeStyleSettingsCustomizable) {
        consumer.showCustomOption(
            settingsClass,
            "ARRAYS_WRAP",
            groupArrays,
            null,
            CfgCodeStyleSettings.WrapSetting.labels,
            CfgCodeStyleSettings.WrapSetting.ordinals
        )
        consumer.showCustomOption(
            settingsClass,
            "ARRAYS_NEWLINE_BODY_OPEN",
            "Place `{` on new line",
            groupArrays
        )
        consumer.showCustomOption(
            settingsClass,
            "ARRAYS_NEWLINE_BODY_CLOSE",
            "Place `}` on new line",
            groupArrays
        )
        consumer.showCustomOption(
            settingsClass,
            "ARRAYS_LEADING_COMMAS",
            "Leading commas",
            groupArrays
        )
        consumer.showCustomOption(
            settingsClass,
            "ARRAYS_ADD_TRAILING_COMMA",
            "Add comma after last element",
            groupArrays
        )

    }

    @Suppress("removal", "DEPRECATION")
    private fun customizeBlankLinesSettings(consumer: CodeStyleSettingsCustomizable) {
/*
        consumer.showCustomOption(
            settingsClass,
            "KEEP_EMPTY_ARRAY_ON_ONE_LINE",
            "Keep empty array on one line",
            CodeStyleSettingsCustomizable.BLANK_LINES //deprecated but works
        )
*/

    }
}

private const val groupArrays = "Arrays"
