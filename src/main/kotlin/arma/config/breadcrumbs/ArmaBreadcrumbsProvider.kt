package arma.config.breadcrumbs

import arma.config.ArmaConfigLanguage
import arma.config.psi.ArmaConfigTypes
import arma.config.psi.ArrayBlock
import arma.config.psi.ClassBlock
import arma.config.psi.ParameterBlock
import com.intellij.lang.Language
import com.intellij.psi.PsiElement
import com.intellij.ui.breadcrumbs.BreadcrumbsProvider
import javax.swing.Action
import javax.swing.Icon

class ArmaBreadcrumbsProvider : BreadcrumbsProvider {

    override fun getLanguages(): Array<Language> = arrayOf(
        Language.findLanguageByID("ArmaConfig") ?: ArmaConfigLanguage
    )

    override fun acceptElement(e: PsiElement): Boolean {
        val type = e.node?.elementType ?: return false

        return when (type) {
            ArmaConfigTypes.CLASS_BLOCK,
            ArmaConfigTypes.PARAMETER_BLOCK,
            ArmaConfigTypes.ARRAY_BLOCK -> true

            else -> false
        }
    }

    override fun getElementInfo(e: PsiElement): String {
        val type = e.node?.elementType

        return when (type) {
            ArmaConfigTypes.CLASS_BLOCK -> {
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

            ArmaConfigTypes.PARAMETER_BLOCK -> {
                // parameterBlock ::= identifier '=' parameterValue ';'
                val param = e as? ParameterBlock
                val nameText = param
                    ?.identifier
                    ?.text
                    ?.takeIf { it.isNotBlank() }

                nameText ?: "<assignment>"
            }

            ArmaConfigTypes.ARRAY_BLOCK -> {
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
