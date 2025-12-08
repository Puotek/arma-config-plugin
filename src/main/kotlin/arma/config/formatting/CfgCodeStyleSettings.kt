package arma.config.formatting

import arma.config.psi.CfgTypes
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType.WHITE_SPACE
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CustomCodeStyleSettings
import com.intellij.rml.dfa.utils.toInt

@Suppress("PropertyName","FunctionName")
class CfgCodeStyleSettings(container: CodeStyleSettings) : CustomCodeStyleSettings("CfgCodeStyleSettings", container) {
    enum class WrapSetting {
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
    var ARRAYS_WRAP: Int = WrapSetting.SMART.ordinal

    @JvmField
    var ARRAYS_OPEN_BODY_NEWLINE: Boolean = false

    @JvmField
    var ARRAYS_CLOSE_BODY_NEWLINE: Boolean = true

    @JvmField
    var ARRAYS_FORCE_TRAILING_COMMA: Boolean = false
    //fixme maybe this as dropdown?
    //todo implement

    @JvmField
    var KEEP_EMPTY_ARRAY_ON_ONE_LINE: Boolean = true

    @JvmField
    var BLANK_LINES_AROUND_CLASS: Int = 1

    @JvmField
    var BLANK_MIN_BETWEEN_CLASS_ELEMENTS: Int = 1

    @JvmField
    var BLANK_MAX_BETWEEN_CLASS_ELEMENTS: Int = 1

    class Viewers(private val raw: CfgCodeStyleSettings) {

        // For smart wrapping, depending on if \n exists between body open and first element -> wrap(true=multiline) or don't
        private fun detectBodyLayout(body: ASTNode): Boolean {
            var firstChild: ASTNode? = null
            var secondChild: ASTNode? = null
            val ignoredTypes = arrayOf(
                WHITE_SPACE,
                CfgTypes.LINE_COMMENT,
                CfgTypes.BLOCK_COMMENT
            )
            var child = body.firstChildNode
            while (child != null) {
                if (child.elementType !in ignoredTypes) if (firstChild == null) firstChild = child else {
                    secondChild = child
                    break
                }
                child = child.treeNext
            }

            // No elements → doesn't matter, treat as MULTILINE by default
            if (firstChild == null || secondChild == null) return true

            // If there is a newline between '{' and first element → multiline layout
            return if (body.psi.containingFile.text.subSequence(
                    firstChild.textRange.endOffset,
                    secondChild.textRange.startOffset
                ).any { it == '\n' }
            ) true else false
        }

        //Helper that depending on current set mode returns whether to wrap (or if Smart it will find out)
        private fun helper(body: ASTNode, option: Int): Boolean = when (WrapSetting.entries[option]) {
            WrapSetting.ONELINE -> false
            WrapSetting.MULTILINE -> true
            WrapSetting.SMART -> detectBodyLayout(body)
        }

        fun ARRAYS_WRAP(body: ASTNode): Int = helper(body, raw.ARRAYS_WRAP).toInt()

        val ARRAYS_OPEN_BODY_NEWLINE: Int get() = raw.ARRAYS_OPEN_BODY_NEWLINE.toInt()

        val ARRAYS_CLOSE_BODY_NEWLINE: Int get() = raw.ARRAYS_CLOSE_BODY_NEWLINE.toInt()

        val ARRAYS_FORCE_TRAILING_COMMA: Int get() = raw.ARRAYS_FORCE_TRAILING_COMMA.toInt()

    }
}
