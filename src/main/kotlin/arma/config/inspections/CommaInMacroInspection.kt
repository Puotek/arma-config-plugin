package arma.config.inspections

import arma.config.psi.ArmaConfigTypes
import arma.config.psi.Identifier
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil

/**
 * Weak warning for any comma token that is located inside macro-like identifiers (with ( ) / ##).
 */
@Suppress("InspectionDescriptionNotFoundInspection")
class CommaInMacroInspection : LocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PsiElementVisitor() {

            override fun visitElement(element: PsiElement) {
                val type = element.node.elementType

                // We only care about comma tokens
                if (type != ArmaConfigTypes.COMMA) return

                // In the new grammar, macros live inside 'identifier'.
                // Commas that sit under an Identifier are inside macro args,
                // commas in arrays / parameter lists are not under Identifier.
                val inIdentifier = PsiTreeUtil.getParentOfType(
                    element,
                    Identifier::class.java,
                    /* strict = */ false
                ) != null

                if (inIdentifier) {
                    holder.registerProblem(element, "Comma inside macro")
                }
            }
        }
    }
}
