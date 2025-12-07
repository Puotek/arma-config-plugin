package arma.config.inspections

import arma.config.psi.CfgTypes
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.TokenType

/**
 * Inspection for any trailing comma in arrays (HEMTT accepts them, BI Tools don't)
 */
@Suppress("InspectionDescriptionNotFoundInspection")
class TrailingCommaInspection : LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PsiElementVisitor() {
            override fun visitElement(element: PsiElement) {
                if (element.node.elementType != CfgTypes.COMMA) return
                if (element.node.treeParent.elementType != CfgTypes.ARRAY_BODY) return
                var inspector = element.nextSibling
                while (inspector != null) {
                    when (inspector.node.elementType) {
                        CfgTypes.RBRACE -> break
                        TokenType.WHITE_SPACE,
                        CfgTypes.LINE_COMMENT,
                        CfgTypes.BLOCK_COMMENT -> inspector = inspector.nextSibling
                        else -> return
                    }
                }
                holder.registerProblem(element, "Trailing comma in array")
            }
        }
    }
}
