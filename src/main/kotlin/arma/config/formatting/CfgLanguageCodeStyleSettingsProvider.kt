package arma.config.formatting

import arma.config.CfgLanguage
import com.intellij.application.options.IndentOptionsEditor
import com.intellij.application.options.SmartIndentOptionsEditor
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable
import com.intellij.psi.codeStyle.CustomCodeStyleSettings
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider

class CfgLanguageCodeStyleSettingsProvider : LanguageCodeStyleSettingsProvider() {

    private val codeStyleSettings = CfgCodeStyleSettings::class.java

    override fun getLanguage() = CfgLanguage
    override fun createCustomSettings(settings: CodeStyleSettings): CustomCodeStyleSettings = CfgCodeStyleSettings(settings)
    override fun getIndentOptionsEditor(): IndentOptionsEditor = SmartIndentOptionsEditor(this)
    override fun getCodeSample(settingsType: SettingsType): String {
        return """
            class NoBody;
            class Import : Parent;
            class Oneline : Parent { XEH(preInit); };
            class Multiline {
                arrayInline[] = {1, 2, 3};
                arrayMultiline[] = {
                    1,
                    2,
                    3
                };
            };
        """.trimIndent()
    }

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
    }

    private fun customizeSpacingSettings(consumer: CodeStyleSettingsCustomizable) {
        consumer.showCustomOption(
            codeStyleSettings,
            "CLASS_SPACE_BEFORE_BODY",
            "Space before `{`",
            groupClass,
        )
        consumer.showCustomOption(
            codeStyleSettings,
            "CLASS_SPACE_COLON_BEFORE",
            "Space before `:`",
            groupClass,
        )
        consumer.showCustomOption(
            codeStyleSettings,
            "CLASS_SPACE_COLON_AFTER",
            "Space after `:`",
            groupClass,
        )
        consumer.showCustomOption(
            codeStyleSettings,
            "CLASS_SPACE_WRAPPED",
            "Space after `{` and before `}`",
            groupClass,
        )

        consumer.showCustomOption(
            codeStyleSettings,
            "ARRAYS_SPACE_EQUALS_BEFORE",
            "Space before `=`",
            groupArrays,
        )
        consumer.showCustomOption(
            codeStyleSettings,
            "ARRAYS_SPACE_EQUALS_AFTER",
            "Space after `=`",
            groupArrays,
        )
    }

    private fun customizeWrappingSettings(consumer: CodeStyleSettingsCustomizable) {
        consumer.showCustomOption(
            codeStyleSettings,
            "CLASS_WRAP",
            groupClass,
            null,
            arrayOf(
                "Wrap always",
                "Based on first element",
            ),
            intArrayOf(
                CfgCodeStyleSettings.WrapSetting.MULTILINE.ordinal,
                CfgCodeStyleSettings.WrapSetting.SMART.ordinal,
            )
        )
        consumer.showCustomOption(
            codeStyleSettings,
            "CLASS_NEWLINE_BODY_OPEN",
            "Place `{` on new line",
            groupClass
        )
        consumer.showCustomOption(
            codeStyleSettings,
            "CLASS_NEWLINE_BODY_CLOSE",
            "Place `}` on new line",
            groupClass
        )
        consumer.showCustomOption(
            codeStyleSettings,
            "ARRAYS_WRAP",
            groupArrays,
            null,
            CfgCodeStyleSettings.WrapSetting.labels,
            CfgCodeStyleSettings.WrapSetting.ordinals
        )
        consumer.showCustomOption(
            codeStyleSettings,
            "ARRAYS_NEWLINE_BODY_OPEN",
            "Place `{` on new line",
            groupArrays
        )
        consumer.showCustomOption(
            codeStyleSettings,
            "ARRAYS_NEWLINE_BODY_CLOSE",
            "Place `}` on new line",
            groupArrays
        )
        consumer.showCustomOption(
            codeStyleSettings,
            "ARRAYS_LEADING_COMMAS",
            "Leading commas",
            groupArrays
        )
/* todo implement
        consumer.showCustomOption(
            codeStyleSettings,
            "ARRAYS_ADD_TRAILING_COMMA",
            "Add comma after last element",
            groupArrays
        )
*/

    }

    //@Suppress("removal", "DEPRECATION")
    private fun customizeBlankLinesSettings(consumer: CodeStyleSettingsCustomizable) {
        consumer.showStandardOptions() //fixme remove
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
private const val groupClass = "Class"
