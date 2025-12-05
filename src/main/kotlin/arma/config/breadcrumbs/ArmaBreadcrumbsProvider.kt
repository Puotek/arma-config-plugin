package arma.config.breadcrumbs

import arma.config.ArmaConfigLanguage
import arma.config.psi.ArmaConfigTypes
import arma.config.psi.ClassBlock
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
        return type == ArmaConfigTypes.CLASS_BLOCK || type == ArmaConfigTypes.PARAMETER_BLOCK || type == ArmaConfigTypes.ARRAY_BLOCK
    }

    // Text shown in breadcrumbs bar
    override fun getElementInfo(e: PsiElement): String {
        val type = e.node?.elementType

        return when (type) {
            ArmaConfigTypes.CLASS_BLOCK -> {
                // Grammar:
                //   classDecl ::= CLASS_KEYWORD classIdent classExt? LBRACE ...
                //   classIdent ::= macroInvocation | IDENT
                //
                // So the name is inside the ClassIdent child now.
                val classDecl = e as? ClassBlock

                // This will be either:
                //  - plain identifier: "MyClass"
                //  - macro invocation: "TAG(MyClass)"
                val fromPsi = classDecl?.className?.text?.takeIf { it.isNotBlank() }

                // Fallback to old behaviour if something is weird
                fromPsi ?: e.node.findChildByType(ArmaConfigTypes.TEXT)?.text ?: "class"
            }

            ArmaConfigTypes.PARAMETER_BLOCK -> {
                // `name = value;` → show "name"
                val ident = e.node.findChildByType(ArmaConfigTypes.TEXT)
                ident?.text ?: "<assignment>"
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
