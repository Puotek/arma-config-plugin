package arma.config.breadcrumbs

import arma.config.CfgLanguage
import arma.config.psi.CfgTypes
import arma.config.psi.ArrayBlock
import arma.config.psi.ClassBlock
import arma.config.psi.ParameterBlock
import com.intellij.lang.Language
import com.intellij.psi.PsiElement
import com.intellij.ui.breadcrumbs.BreadcrumbsProvider
import javax.swing.Action
import javax.swing.Icon

class CfgBreadcrumbsProvider : BreadcrumbsProvider {

    override fun getLanguages(): Array<Language> = arrayOf(
        Language.findLanguageByID("ArmaConfig") ?: CfgLanguage
    )

    override fun acceptElement(e: PsiElement): Boolean {
        val type = e.node?.elementType ?: return false

        return when (type) {
            CfgTypes.CLASS_BLOCK,
            CfgTypes.PARAMETER_BLOCK,
            CfgTypes.ARRAY_BLOCK -> true

            else -> false
        }
    }

    override fun getElementInfo(e: PsiElement): String {
        val type = e.node?.elementType

        return when (type) {
            CfgTypes.CLASS_BLOCK -> {
                val classDecl = e as? ClassBlock

                // In new BNF: classBlock ::= CLASS_KEYWORD identifier classExtension? ...
                val ownName = classDecl
                    ?.identifier
                    ?.text
                    ?.takeIf { it.isNotBlank() }
                    ?: "<class>"

                // classExtension ::= COLON identifier
                val parentName = classDecl
                    ?.classExtension
                    ?.identifier
                    ?.text
                    ?.takeIf { it.isNotBlank() }

                if (parentName != null) {
                    "$ownName : $parentName"
                } else {
                    ownName
                }
            }

            CfgTypes.PARAMETER_BLOCK -> {
                // parameterBlock ::= identifier '=' parameterValue ';'
                val param = e as? ParameterBlock
                val nameText = param
                    ?.identifier
                    ?.text
                    ?.takeIf { it.isNotBlank() }

                nameText ?: "<assignment>"
            }

            CfgTypes.ARRAY_BLOCK -> {
                // arrayBlock ::= identifier '[]' ... -> show "name[]"
                val array = e as? ArrayBlock
                val nameText = array
                    ?.identifier
                    ?.text
                    ?.takeIf { it.isNotBlank() }

                if (nameText != null) {
                    "$nameText[]"
                } else {
                    "<array>"
                }
            }

            else -> {
                e.text.take(30).replace('\n', ' ')
            }
        }
    }

    override fun getElementTooltip(e: PsiElement): String? = null
    override fun getElementIcon(e: PsiElement): Icon? = null
    override fun getChildren(e: PsiElement): List<PsiElement> = emptyList()
    override fun getContextActions(e: PsiElement): List<Action> = emptyList()
    override fun getParent(e: PsiElement): PsiElement? = e.parent
    override fun isShownByDefault(): Boolean = true
}
