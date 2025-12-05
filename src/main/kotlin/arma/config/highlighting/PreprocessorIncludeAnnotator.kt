package arma.config.highlighting

import arma.config.psi.ArmaConfigTypes
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement

class PreprocessorIncludeAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        // Only interested in preprocessor tokens
        if (element.node.elementType != ArmaConfigTypes.PREPROCESSOR) return

        val fullText = element.text
        if (fullText.isEmpty()) return

        // In lexer we enforced '#'+letters, but be robust and trim left anyway
        val trimmed = fullText.trimStart()
        val leadingWs = fullText.length - trimmed.length

        if (!trimmed.startsWith("#include")) return

        // 1) Highlight the 'include' keyword
        val includeIndexInTrimmed = trimmed.indexOf("include")
        if (includeIndexInTrimmed < 0) return

        // 2) Highlight the path inside quotes (or <...>) as STRING-ish

        var i = includeIndexInTrimmed + "include".length
        // Skip whitespace after 'include'
        while (i < trimmed.length && trimmed[i].isWhitespace()) i++
        if (i >= trimmed.length) return

        val delimiter = trimmed[i]
        if (delimiter != '"' && delimiter != '<') return
        val closing = if (delimiter == '"') '"' else '>'

        val pathStartInTrimmed = i + 1
        var j = pathStartInTrimmed
        while (j < trimmed.length) {
            val ch = trimmed[j]
            if (ch == closing || ch == '\n' || ch == '\r') break
            j++
        }
        if (j <= pathStartInTrimmed || j >= trimmed.length || trimmed[j] != closing) return

        val pathStartInElement = leadingWs + pathStartInTrimmed -1
        val pathEndInElement = leadingWs + j +1

        holder.newSilentAnnotation(HighlightSeverity.INFORMATION).range(
            TextRange(
                element.textRange.startOffset + pathStartInElement, element.textRange.startOffset + pathEndInElement
            )
        )
            // Reuse the normal STRING color for the include path
            .textAttributes(ArmaConfigSyntaxHighlighter.STRING).create()
    }
}
