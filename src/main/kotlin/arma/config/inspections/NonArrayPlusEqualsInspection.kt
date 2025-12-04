package arma.config.inspections

import arma.config.psi.ArmaConfigTypes
import arma.config.psi.Assignment
import arma.config.psi.Visitor
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor

/**
 * Flags uses of "+=" where the left side is NOT an array property (no [] suffix).
 *
 * Example (OK):
 *   myArray[] += {1, 2};
 *
 * Examples (ERROR):
 *   value += 1;
 *   value += {1, 2};
 */
class NonArrayPlusEqualsInspection : LocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : Visitor() {
            override fun visitAssignment(o: Assignment) {
                super.visitAssignment(o)

                val assignmentNode = o.node

                // Look for the PLUS that forms the "+=" of this assignment.
                // NOTE: findChildByType is *non-recursive*, so we won't see PLUS inside the value expr.
                val plusNode = assignmentNode.findChildByType(ArmaConfigTypes.PLUS) ?: return
                val equalNode = assignmentNode.findChildByType(ArmaConfigTypes.EQUAL) ?: return

                // We only care about "+=" assignments; normal "=" is fine.
                // (Grammar guarantees that if PLUS is a direct child, it's the "+=" operator.)
                val hasPlusEquals =
                    plusNode.treeNext?.elementType == ArmaConfigTypes.EQUAL || equalNode.treePrev?.elementType == ArmaConfigTypes.PLUS

                if (!hasPlusEquals) return

                // If there is no arraySuffix on the LHS, then this is a "normal" property using "+="
                if (o.arraySuffix == null) {
                    holder.registerProblem(
                        plusNode.psi,
                        "Use of `+=` is only allowed for array properties (identifier[] += {...})",
                        ProblemHighlightType.GENERIC_ERROR_OR_WARNING
                    )
                }
            }
        }
    }
}
