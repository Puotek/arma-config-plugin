package arma.config.breadcrumbs

import com.intellij.lang.Language
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.ui.breadcrumbs.BreadcrumbsProvider
import arma.config.ArmaConfigLanguage

class ArmaBreadcrumbsProvider : BreadcrumbsProvider {

    override fun getLanguages(): Array<Language> =
        arrayOf(ArmaConfigLanguage)

    override fun acceptElement(e: PsiElement): Boolean {
        // Any element in an Arma Config file except the file root
        val file = e.containingFile ?: return false
        if (file.language != ArmaConfigLanguage) return false
        if (e is PsiFile) return false
        return true
    }

    override fun getElementInfo(e: PsiElement): String {
        val text = e.text.replace("\n", " ")
        return if (text.length > 30) text.substring(0, 30) + "…" else text
    }

    override fun getElementTooltip(e: PsiElement): String? =
        e.text
}
