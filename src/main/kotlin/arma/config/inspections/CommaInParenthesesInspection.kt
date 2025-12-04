package arma.config.inspections

import arma.config.psi.ArmaConfigTypes
import arma.config.psi.MacroInner
import com.intellij.codeInspection.LocalInspectionTool
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
class CommaInParenthesesInspection : LocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PsiElementVisitor() {
            override fun visitElement(element: PsiElement) {
                val type = element.node.elementType

                // We only care about comma tokens
                if (type != ArmaConfigTypes.COMMA) return

                // Check if the comma lives somewhere inside a MacroInner
                val commaInMacro = PsiTreeUtil.getParentOfType(element, MacroInner::class.java, false)

                if (commaInMacro != null) holder.registerProblem(element, "Comma inside parentheses")
            }
        }
    }
}
