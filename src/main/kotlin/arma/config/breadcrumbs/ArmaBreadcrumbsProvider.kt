package arma.config.breadcrumbs

import arma.config.ArmaConfigLanguage     // Our language
import arma.config.psi.ArmaConfigTypes   // Token/element types for PSI
import com.intellij.lang.Language
import com.intellij.psi.PsiElement
import com.intellij.ui.breadcrumbs.BreadcrumbsProvider
import javax.swing.Action
import javax.swing.Icon

/**
 * Breadcrumbs + sticky lines support for Arma config.
 *
 * Sticky Lines uses this breadcrumb structure to decide which lines
 * to pin at the top of the editor as you scroll.
 */
class ArmaBreadcrumbsProvider : BreadcrumbsProvider {

    // Which languages this provider applies to
    override fun getLanguages(): Array<Language> =
        arrayOf(ArmaConfigLanguage)

    // Decide which PSI elements should appear as breadcrumb nodes
    override fun acceptElement(e: PsiElement): Boolean {
        val type = e.node?.elementType ?: return false

        // Breadcrumb elements (and thus sticky lines) are created for:
        // - class declarations
        // - assignments (e.g. variable = value;)
        return type == ArmaConfigTypes.CLASS_DECL ||
                type == ArmaConfigTypes.ASSIGNMENT
    }

    // Text representation shown in breadcrumbs bar (and used in sticky lines)
    override fun getElementInfo(e: PsiElement): String {
        val type = e.node?.elementType

        return when (type) {
            ArmaConfigTypes.CLASS_DECL -> {
                // For a class declaration, try to get its IDENT child: `class Name`
                val ident = e.node.findChildByType(ArmaConfigTypes.IDENT)
                val name = ident?.text ?: "class"
                "class $name"
            }

            ArmaConfigTypes.ASSIGNMENT -> {
                // For assignments, show the variable name (IDENT child)
                val ident = e.node.findChildByType(ArmaConfigTypes.IDENT)
                ident?.text ?: "<assignment>"
            }

            else -> e.text // Fallback: show raw text
        }
    }

    // Tooltip when hovering a breadcrumb; we don't use it
    override fun getElementTooltip(e: PsiElement): String? =
        null

    // Icon for a breadcrumb; return null for no icon
    override fun getElementIcon(e: PsiElement): Icon? =
        null

    // Children of a breadcrumb element (for hierarchical breadcrumb tree)
    // Here we make it a flat structure and return none
    override fun getChildren(e: PsiElement): List<PsiElement> =
        emptyList()

    // Context actions when right-clicking breadcrumb; we don't provide any
    override fun getContextActions(e: PsiElement): List<out Action> =
        emptyList()

    // Parent element in breadcrumb hierarchy; we just use PSI parent
    override fun getParent(e: PsiElement): PsiElement? =
        e.parent

    // Whether breadcrumbs for this provider are shown by default
    override fun isShownByDefault(): Boolean = true
}
