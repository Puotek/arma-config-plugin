package arma.config.inspections

import arma.config.psi.CfgTypes
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor

/**
 * Inspection for identifiers starting with a number
 */
@Suppress("InspectionDescriptionNotFoundInspection")
class IdentifierStartingWithNumber : LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PsiElementVisitor() {
            override fun visitElement(element: PsiElement) {
                if (element.node.elementType == CfgTypes.IDENTIFIER &&
                    element.node.text[0] in '0'..'9'
                ) holder.registerProblem(element, "Identifier starting with number")
            }
        }
    }
}
