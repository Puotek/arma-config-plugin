package arma.config.inspections

import arma.config.psi.ArmaConfigTypes
import arma.config.psi.MacroBlock
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil

/**
 * Weak warning for any comma token that is located inside macro parentheses.
 */
@Suppress("InspectionDescriptionNotFoundInspection")
class CommaInMacroInspection : LocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PsiElementVisitor() {
            override fun visitElement(element: PsiElement) {
                val type = element.node.elementType

                // We only care about comma tokens
                if (type != ArmaConfigTypes.COMMA) return

                // Check if the comma lives somewhere inside a macro
                if (PsiTreeUtil.getParentOfType(element, MacroBlock::class.java, false) != null) holder.registerProblem(element, "Comma inside macro")
            }
        }
    }
}
