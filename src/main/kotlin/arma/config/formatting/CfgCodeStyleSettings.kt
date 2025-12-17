package arma.config.formatting

import arma.config.psi.CfgTypes
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType.WHITE_SPACE
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CustomCodeStyleSettings
import com.intellij.rml.dfa.utils.toInt

@Suppress("PropertyName", "FunctionName", "unused")
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
    var CLASS_WRAP: Int = WrapSetting.SMART.ordinal

    @JvmField
    var CLASS_NEWLINE_BODY_OPEN: Boolean = false

    @JvmField
    var CLASS_NEWLINE_BODY_CLOSE: Boolean = true

    //fixme rename to COLLAPSE_EMPTY_BODY
    @JvmField
    var CLASS_KEEP_EMPTY_BODY_ONELINE: Boolean = true

    @JvmField
    var CLASS_SPACE_BEFORE_BODY: Boolean = true

    @JvmField
    var CLASS_SPACE_COLON_BEFORE: Boolean = true

    @JvmField
    var CLASS_SPACE_COLON_AFTER: Boolean = true

    @JvmField
    var CLASS_SPACE_WRAPPED: Boolean = true

    @JvmField
    var ARRAYS_WRAP: Int = WrapSetting.SMART.ordinal

    @JvmField
    var ARRAYS_NEWLINE_BODY_OPEN: Boolean = false

    @JvmField
    var ARRAYS_NEWLINE_BODY_CLOSE: Boolean = true

    //fixme rename to COLLAPSE_EMPTY_BODY
    @JvmField
    var ARRAYS_KEEP_EMPTY_BODY_ONELINE: Boolean = true

    @JvmField
    var ARRAYS_LEADING_COMMAS: Boolean = false

    @JvmField
    var ARRAYS_ADD_TRAILING_COMMA: Boolean = false /*
    fixme maybe this as dropdown?
    todo implement
    */

    @JvmField
    var ARRAYS_SPACE_EQUALS_BEFORE: Boolean = true

    @JvmField
    var ARRAYS_SPACE_EQUALS_AFTER: Boolean = true

    @JvmField
    var PARAMETER_SPACE_EQUALS_BEFORE: Boolean = true

    @JvmField
    var PARAMETER_SPACE_EQUALS_AFTER: Boolean = true

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
            return body.psi.containingFile.text.subSequence(
                firstChild.textRange.endOffset,
                secondChild.textRange.startOffset
            ).any { it == '\n' }
        }

        //Helper that depending on current set mode returns whether to wrap (or if Smart it will find out)
        private fun helper(body: ASTNode, option: Int): Boolean = when (WrapSetting.entries[option]) {
            WrapSetting.ONELINE -> false
            WrapSetting.MULTILINE -> true
            WrapSetting.SMART -> detectBodyLayout(body)
        }

        fun CLASS_WRAP(body: ASTNode): Boolean = helper(body, raw.CLASS_WRAP)

        val CLASS_NEWLINE_BODY_OPEN: Boolean get() = raw.CLASS_NEWLINE_BODY_OPEN

        val CLASS_NEWLINE_BODY_CLOSE: Boolean get() = raw.CLASS_NEWLINE_BODY_CLOSE

        val CLASS_SPACE_COLON_BEFORE: Boolean get() = raw.CLASS_SPACE_COLON_BEFORE

        val CLASS_SPACE_COLON_AFTER: Boolean get() = raw.CLASS_SPACE_COLON_AFTER

        val CLASS_SPACE_BEFORE_BODY: Boolean get() = raw.CLASS_SPACE_BEFORE_BODY

        val CLASS_SPACE_WRAPPED: Boolean get() = raw.CLASS_SPACE_WRAPPED

        fun ARRAYS_WRAP(body: ASTNode): Boolean = helper(body, raw.ARRAYS_WRAP)

        val ARRAYS_NEWLINE_BODY_OPEN: Boolean get() = raw.ARRAYS_NEWLINE_BODY_OPEN

        val ARRAYS_NEWLINE_BODY_CLOSE: Boolean get() = raw.ARRAYS_NEWLINE_BODY_CLOSE

        val ARRAYS_LEADING_COMMAS: Boolean get() = raw.ARRAYS_LEADING_COMMAS

        val ARRAYS_ADD_TRAILING_COMMA: Boolean get() = !raw.ARRAYS_LEADING_COMMAS && raw.ARRAYS_ADD_TRAILING_COMMA

        val ARRAYS_SPACE_EQUALS_BEFORE: Int get() = raw.ARRAYS_SPACE_EQUALS_BEFORE.toInt()

        val ARRAYS_SPACE_EQUALS_AFTER: Int get() = raw.ARRAYS_SPACE_EQUALS_AFTER.toInt()

    }
}
