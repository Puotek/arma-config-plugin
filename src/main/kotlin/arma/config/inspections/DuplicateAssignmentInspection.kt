package arma.config.inspections

import arma.config.psi.ClassDecl
import arma.config.psi.Assignment
import arma.config.psi.ArmaConfigTypes
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil

class DuplicateAssignmentInspection : LocalInspectionTool() {

    override fun getDisplayName(): String =
        "Duplicate parameter assignment in class"

    override fun getShortName(): String =
        "ArmaDuplicateAssignment"

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PsiElementVisitor() {

            override fun visitElement(element: PsiElement) {
                if (element is ClassDecl) {
                    checkClass(element, holder)
                }
            }
        }
    }

    private fun checkClass(classDecl: ClassDecl, holder: ProblemsHolder) {
        // Find all Assignment elements under this class
        val allAssignments = PsiTreeUtil.findChildrenOfType(classDecl, Assignment::class.java)

        // We only want assignments whose *nearest* ClassDecl parent is this class
        val directAssignments = allAssignments.filter { assignment ->
            PsiTreeUtil.getParentOfType(assignment, ClassDecl::class.java, /* strict = */ false) == classDecl
        }

        // Map from parameter name -> first assignment element
        val firstByName = mutableMapOf<String, Assignment>()

        for (assignment in directAssignments) {
            val identNode = assignment.node.findChildByType(ArmaConfigTypes.IDENT)
            val name = identNode?.text ?: continue

            val existing = firstByName[name]
            if (existing == null) {
                // First time we see this parameter name
                firstByName[name] = assignment
            } else {
                // Duplicate: register a problem on the *identifier* of this assignment
                val problemTarget = identNode.psi ?: assignment
                holder.registerProblem(
                    problemTarget,
                    "Duplicate assignment to '$name' in this class"
                )
            }
        }
    }
}
