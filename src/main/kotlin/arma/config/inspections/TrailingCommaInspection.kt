package arma.config.inspections

import arma.config.psi.ArrayBody
import arma.config.psi.CfgTypes
import arma.config.psi.Identifier
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil

/**
 * Inspection for any trailing comma in arrays (HEMTT accepts them, BI Tools don't)
 */
@Suppress("InspectionDescriptionNotFoundInspection")
class TrailingCommaInspection : LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PsiElementVisitor() {
            override fun visitElement(element: PsiElement) {
                if (element.node.elementType != CfgTypes.COMMA) return
                if (element.nextSibling.node.elementType != CfgTypes.RBRACE) return
                if (element.node.treeParent.elementType == CfgTypes.ARRAY_BODY) holder.registerProblem(element, "Trailing comma in array")
            }
        }
    }
}
