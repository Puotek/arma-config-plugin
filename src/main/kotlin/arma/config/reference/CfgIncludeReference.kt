package arma.config.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiReferenceBase

class CfgIncludeReference(element: PsiElement, rangeInElement: TextRange) : PsiReferenceBase<PsiElement>(element, rangeInElement, true) {
    override fun getVariants(): Array<Any> = emptyArray()
    override fun resolve(): PsiElement? {
        val rawPath = rangeInElement.substring(element.text)
        if (rawPath.isEmpty()) return null
        val sanitizedPath = rawPath
            .replace("\t", "%9")
            .replace("?", "%3f")
            .replace('\\', '/')
            .trim()
        if (sanitizedPath.isBlank()) return null

        val baseDir = element.containingFile.virtualFile.parent ?: return null

        fun resolveRelative(rel: String): PsiElement? {
            return PsiManager.getInstance(element.project).findFile(baseDir.findFileByRelativePath(rel) ?: return null)
        }

        resolveRelative(sanitizedPath)?.let { return it }

        if (sanitizedPath.startsWith("/")) {
            val parts = sanitizedPath.removePrefix("/").split('/').filter { it.isNotEmpty() }
            if (parts.size > 2) {
                val dropped = parts.drop(2).joinToString("/")
                resolveRelative(dropped)?.let { return it }
            }
        }
        return null
    }
}
