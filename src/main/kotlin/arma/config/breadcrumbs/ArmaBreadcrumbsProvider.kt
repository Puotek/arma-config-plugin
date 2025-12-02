package arma.config.breadcrumbs

import arma.config.ArmaConfigLanguage
import com.intellij.lang.Language
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.ui.breadcrumbs.BreadcrumbsProvider

class ArmaBreadcrumbsProvider : BreadcrumbsProvider {

    override fun getLanguages(): Array<Language> =
        arrayOf(ArmaConfigLanguage)

    override fun acceptElement(e: PsiElement): Boolean {
        val file = e.containingFile ?: return false
        if (file.language != ArmaConfigLanguage) return false
        if (e is PsiFile) return false
        return true
    }

    override fun getElementInfo(e: PsiElement): String {
        val type = e.node?.elementType?.toString() ?: "<no-type>"
        return "${e.javaClass.simpleName}:$type"
    }

    override fun getElementTooltip(e: PsiElement): String? =
        e.text
}
