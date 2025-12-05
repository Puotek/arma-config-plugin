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

    // Which languages this provider applies to
    override fun getLanguages(): Array<Language> = arrayOf(
        // use the registered language instance if possible
        Language.findLanguageByID("ArmaConfig") ?: ArmaConfigLanguage
    )

    // Decide which PSI elements should appear as breadcrumb nodes
    override fun acceptElement(e: PsiElement): Boolean {
        val type = e.node?.elementType ?: return false

        // Breadcrumb elements (and thus sticky lines) are created for:
        // - class declarations
        // - assignments (e.g. variable = value;)
        // - array assignments (name[] = { ... };)
        return when (type) {
            ArmaConfigTypes.CLASS_BLOCK,
            ArmaConfigTypes.PARAMETER_BLOCK,
            ArmaConfigTypes.ARRAY_BLOCK -> true

            else -> false
        }
    }

    // Text shown in breadcrumbs bar
    override fun getElementInfo(e: PsiElement): String {
        val type = e.node?.elementType

        return when (type) {
            ArmaConfigTypes.CLASS_BLOCK -> {
                val classDecl = e as? ClassBlock

                // Own name: TEXT or macroBlock – text covers both
                val ownName = classDecl
                    ?.className
                    ?.text
                    ?.takeIf { it.isNotBlank() }

                // Parent from inheritance: ": ParentClass"
                val parentName = classDecl
                    ?.classExtension
                    ?.className
                    ?.text
                    ?.takeIf { it.isNotBlank() }

                val base = ownName
                    ?: e.node.findChildByType(ArmaConfigTypes.TEXT)?.text
                    ?: "class"

                if (parentName != null) {
                    "$base : $parentName"
                } else {
                    base
                }
            }

            ArmaConfigTypes.PARAMETER_BLOCK -> {
                // `name = value;` → show "name" (supports macros in parameterName)
                val param = e as? ParameterBlock
                val nameText = param
                    ?.parameterName
                    ?.text
                    ?.takeIf { it.isNotBlank() }

                nameText ?: "<assignment>"
            }

            ArmaConfigTypes.ARRAY_BLOCK -> {
                // array[] = { ... }; → show "array[]"
                // parameterName can be TEXT or macroBlock; .text handles both
                val array = e as? ArrayBlock
                val nameText = array
                    ?.parameterName
                    ?.text
                    ?.takeIf { it.isNotBlank() }

                if (nameText != null) {
                    "$nameText[]"
                } else {
                    "<array>"
                }
            }

            else -> {
                // Shouldn’t be hit if acceptElement() is correct, but just in case:
                e.text.take(30).replace('\n', ' ')
            }
        }
    }

    override fun getElementTooltip(e: PsiElement): String? = null

    override fun getElementIcon(e: PsiElement): Icon? = null

    override fun getChildren(e: PsiElement): List<PsiElement> = emptyList()

    override fun getContextActions(e: PsiElement): List<Action> = emptyList()

    // Default is fine: breadcrumbs infra will walk PSI parents and
    // then filter them via acceptElement()
    override fun getParent(e: PsiElement): PsiElement? = e.parent

    override fun isShownByDefault(): Boolean = true
}
