package arma.config.highlighting

import arma.config.psi.CfgTypes
import arma.config.psi.MacroBody
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType

class MacroAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element !is MacroBody) return
        val highlightKey = CfgSyntaxHighlighter.MACRO

        if (element.firstChild.nextSibling.elementType != CfgTypes.LPAREN) return

        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
            .range(element.firstChild.textRange)
            .textAttributes(highlightKey)
            .create()

        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
            .range(element.firstChild.nextSibling.textRange)
            .textAttributes(highlightKey)
            .create()

        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
            .range(element.lastChild.textRange)
            .textAttributes(highlightKey)
            .create()
    }
}
