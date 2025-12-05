package arma.config.reference

import arma.config.psi.ArmaConfigTypes
import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.util.ProcessingContext

class ArmaConfigReferenceContributor : PsiReferenceContributor() {

    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement().withElementType(ArmaConfigTypes.PREPROCESSOR),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(
                    element: PsiElement,
                    context: ProcessingContext
                ): Array<PsiReference> {

                    val fullText = element.text
                    if (fullText.isEmpty()) return PsiReference.EMPTY_ARRAY

                    val trimmed = fullText.trimStart()
                    val leadingWs = fullText.length - trimmed.length

                    if (!trimmed.startsWith("#include")) {
                        return PsiReference.EMPTY_ARRAY
                    }

                    val includeIndexInTrimmed = trimmed.indexOf("include")
                    if (includeIndexInTrimmed < 0) return PsiReference.EMPTY_ARRAY

                    var i = includeIndexInTrimmed + "include".length
                    // skip whitespace after 'include'
                    while (i < trimmed.length && trimmed[i].isWhitespace()) i++
                    if (i >= trimmed.length) return PsiReference.EMPTY_ARRAY

                    val delimiter = trimmed[i]
                    if (delimiter != '"' && delimiter != '<') return PsiReference.EMPTY_ARRAY
                    val closing = if (delimiter == '"') '"' else '>'

                    val pathStartInTrimmed = i + 1
                    var j = pathStartInTrimmed
                    while (j < trimmed.length) {
                        val ch = trimmed[j]
                        if (ch == closing || ch == '\n' || ch == '\r') break
                        j++
                    }
                    if (j <= pathStartInTrimmed || j >= trimmed.length || trimmed[j] != closing) {
                        return PsiReference.EMPTY_ARRAY
                    }

                    val pathStartInElement = leadingWs + pathStartInTrimmed
                    val pathEndInElement = leadingWs + j

                    val rangeInElement = TextRange(pathStartInElement, pathEndInElement)

                    return arrayOf(
                        ArmaConfigIncludeReference(element, rangeInElement)
                    )
                }
            }
        )
    }
}
