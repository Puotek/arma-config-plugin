package arma.config.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.PsiReferenceBase

class CfgIncludeReference(
    element: PsiElement,
    rangeInElement: TextRange
) : PsiReferenceBase<PsiElement>(element, rangeInElement, /* soft = */ true) {

    override fun resolve(): PsiElement? {
        val rawPath = rangeInElement.substring(element.text).trim()
        if (rawPath.isBlank()) return null

        // Convert Arma-style backslashes → forward slashes
        val normalized = rawPath.replace('\\', '/').trim()

        val containingFile = element.containingFile ?: return null
        val vFile = containingFile.virtualFile ?: return null
        val baseDir = vFile.parent ?: return null

        val psiManager = PsiManager.getInstance(element.project)

        // Helper to try resolving a relative path under baseDir
        fun resolveRelative(rel: String?): PsiElement? {
            if (rel.isNullOrBlank()) return null
            val v = baseDir.findFileByRelativePath(rel) ?: return null
            return psiManager.findFile(v)
        }

        // 1) First, try the normalized path as-is
        resolveRelative(normalized)?.let { return it }

        // 2) If the original path started with '\' (Arma-style absolute-ish),
        //    drop the first TWO segments and try again.
        //
        // Example:
        //   raw: "\block1\block2\rest\file.hpp"
        //   normalized: "/block1/block2/rest/file.hpp"
        //   -> drop "block1", "block2" -> "rest/file.hpp"
        if (rawPath.startsWith("\\")) {
            val trimmedLeadingSlash = normalized.removePrefix("/")
            val parts = trimmedLeadingSlash.split('/').filter { it.isNotEmpty() }
            if (parts.size > 2) {
                val dropped = parts.drop(2).joinToString("/")
                resolveRelative(dropped)?.let { return it }
            }
        }

        // 3) As a last resort, try raw path (in case someone already used '/' etc.)
        resolveRelative(rawPath.replace('\\', '/'))?.let { return it }

        return null
    }

    override fun getVariants(): Array<Any> = emptyArray()
}
