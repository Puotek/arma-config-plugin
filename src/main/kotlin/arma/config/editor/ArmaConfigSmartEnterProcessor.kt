package arma.config.editor

import arma.config.psi.ArmaConfigFile
import arma.config.psi.ArmaConfigTypes
import com.intellij.codeInsight.editorActions.smartEnter.SmartEnterProcessor
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile

/**
 * Smart "complete statement" logic for Arma Config.
 *
 * Invoked by Edit | Complete Statement (EP: com.intellij.lang.smartEnterProcessor).
 * We work line-based:
 *
 *  - If current line is non-empty and doesn't already end with ';',
 *    we append ';' at the logical end of the line.
 *  - If it ends with '}', we also append ';' (for class bodies).
 *
 * We skip lines that are clearly preprocessor / single-quote blocks.
 *
 * IMPORTANT: we always return false, so the default "plain enter" still runs,
 * moving the caret to the next line and applying normal indent.
 */
class ArmaConfigSmartEnterProcessor : SmartEnterProcessor() {

    override fun process(project: Project, editor: Editor, file: PsiFile): Boolean {
        val armaFile = file as? ArmaConfigFile ?: return false

        val document = editor.document
        val text = document.charsSequence
        val caretOffset = editor.caretModel.offset

        if (caretOffset < 0 || caretOffset > text.length) return false

        val lineNumber = document.getLineNumber(caretOffset)
        val lineStart = document.getLineStartOffset(lineNumber)
        val lineEnd = document.getLineEndOffset(lineNumber)

        if (lineStart >= lineEnd) return false

        // Trim trailing whitespace
        var end = lineEnd
        while (end > lineStart && text[end - 1].isWhitespace()) {
            end--
        }
        if (end <= lineStart) return false

        val lastCharOffset = end - 1
        val element = armaFile.findElementAt(lastCharOffset) ?: return false
        val tokenType = element.node.elementType

        // Don't add ';' on pure preprocessor lines or single-quote blocks
        if (tokenType == ArmaConfigTypes.PREPROCESSOR ||
            tokenType == ArmaConfigTypes.SINGLE_QUOTE_BLOCK
        ) {
            return false
        }

        val lastChar = text[lastCharOffset]

        // Already has ';' at end => nothing to do
        if (lastChar == ';') return false

        // For lines ending with '}', we want `};`
        if (lastChar == '}') {
            document.insertString(end, ";")
            // return false so default "plain enter" still runs
            return false
        }

        // Generic case: non-empty line that doesn't end with ';' => append it
        document.insertString(end, ";")
        // return false so default "plain enter" still runs
        return false
    }
}
