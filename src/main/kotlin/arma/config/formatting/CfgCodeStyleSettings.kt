package arma.config.formatting

import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CustomCodeStyleSettings

class CfgCodeStyleSettings(container: CodeStyleSettings) : CustomCodeStyleSettings("CfgCodeStyleSettings", container) {

    @JvmField
    var WRAP_ARRAYS: Int = ArrayLayoutMode.SMART.ordinal
    enum class ArrayLayoutMode {
        ONELINE,
        MULTILINE,
        SMART;

        companion object {
            val ordinals: IntArray = entries.map { it.ordinal }.toIntArray()
            val labels: Array<String> = arrayOf(
                "Do not wrap",
                "Wrap always",
                "Based on first element",
            )
        }
    }


    @JvmField
    var KEEP_EMPTY_ARRAY_ON_ONE_LINE: Boolean = true

    @JvmField
    var BLANK_LINES_AROUND_CLASS: Int = 1
}
