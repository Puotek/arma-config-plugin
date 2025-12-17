package arma.config.inspections

import arma.config.psi.CfgTypes
import arma.config.reference.CfgIncludeReference
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor

@Suppress("InspectionDescriptionNotFoundInspection")
class UnresolvedIncludeInspection : LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PsiElementVisitor() {
            override fun visitElement(element: PsiElement) {
                if (element.node.elementType != CfgTypes.PREPROCESSOR) return
                val includeRef = element.references.firstOrNull { it is CfgIncludeReference } as? CfgIncludeReference ?: return
                if (includeRef.resolve() != null) return
                holder.registerProblem(
                    element,
                    includeRef.rangeInElement,
                    "File not found"
                )
            }
        }
    }
}
