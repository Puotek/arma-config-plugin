package arma.config.inspections

import arma.config.psi.ArmaConfigTypes
import arma.config.psi.MacroInner
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil

/**
 * Weak warning for any comma token that is located inside parentheses.
 *
 * In the current grammar, parentheses only appear in macro invocations:
 *
 *   macroInvocation ::= IDENT LPAREN macroInner? RPAREN
 *
 * and the content is represented as MacroInner / MacroInnerToken nodes.
 *
 * So our rule is:
 *   - If a COMMA token has a MacroInner ancestor -> show a WEAK_WARNING.
 */
@Suppress("InspectionDescriptionNotFoundInspection")
class CommaInParenthesesInspection : LocalInspectionTool() {

    override fun getDisplayName(): String =
        "Comma inside parentheses"

    override fun getShortName(): String =
        "ArmaCommaInParentheses"

    override fun getGroupDisplayName(): String =
        "Arma Config"

    override fun isEnabledByDefault(): Boolean = true

    override fun buildVisitor(
        holder: ProblemsHolder,
        isOnTheFly: Boolean
    ): PsiElementVisitor {
        return object : PsiElementVisitor() {
            override fun visitElement(element: PsiElement) {
                val node = element.node ?: return

                // We only care about comma tokens
                if (node.elementType != ArmaConfigTypes.COMMA) return

                // Check if the comma lives somewhere inside a MacroInner
                val macroInner = PsiTreeUtil.getParentOfType(element, MacroInner::class.java, /* strict = */ false)
                if (macroInner != null) {
                    holder.registerProblem(
                        element,
                        "Comma inside parentheses",
                        ProblemHighlightType.WEAK_WARNING
                    )
                }
            }
        }
    }
}
