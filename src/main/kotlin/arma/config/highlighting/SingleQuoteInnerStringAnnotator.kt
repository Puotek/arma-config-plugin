package arma.config.highlighting

import arma.config.psi.ArmaConfigTypes
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement

class SingleQuoteInnerStringAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        // Only care about the PSI node for singleQuoteBlock
        if (element.node.elementType != ArmaConfigTypes.SINGLE_QUOTE) return

        val text = element.text
        if (text.length < 3) return // must be at least 'x'

        // Scan inside the block for "..." sequences, respecting Arma string rules
        var i = 0
        while (i < text.length) {
            val ch = text[i]
            if (ch == '"') {
                val startInBlock = i
                var j = i + 1

                while (j < text.length) {
                    val cj = text[j]

                    if (cj == '"') {
                        // "" inside -> escaped quote, skip both
                        if (j + 1 < text.length && text[j + 1] == '"') {
                            j += 2
                            continue
                        }
                        // Single " closes the string
                        j++
                        break
                    }

                    // We know there is no newline in SINGLE_QUOTE_BLOCK_TOKEN by lexer design
                    j++
                }

                // Convert from local text indices to file range
                val globalStart = element.textRange.startOffset + startInBlock
                val globalEnd = element.textRange.startOffset + j

                val range = TextRange(globalStart, globalEnd)

                holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .range(range)
                    .textAttributes(ArmaConfigSyntaxHighlighter.STRING)
                    .create()

                i = j
            } else {
                i++
            }
        }
    }
}
