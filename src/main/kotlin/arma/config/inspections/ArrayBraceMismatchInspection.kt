package arma.config.inspections

import arma.config.psi.Array
import arma.config.psi.ArraySuffix
import arma.config.psi.Assignment
import arma.config.psi.Value
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor

/**
 * Checks that array assignments use matching syntax:
 *
 *   name[] = { ... };  // OK
 *   name   = value;    // OK, value is not { ... }
 *
 * and flags:
 *
 *   name[] = 1;        // [] but no { }
 *   name   = { 1, 2 }; // { } but no []
 */
@Suppress("InspectionDescriptionNotFoundInspection")
class ArrayBraceMismatchInspection : LocalInspectionTool() {

    override fun getDisplayName(): String =
        "Array brackets and braces must match"

    override fun getShortName(): String =
        "ArmaArrayBraceMismatch"

    override fun getGroupDisplayName(): String =
        "Arma Config"

    override fun isEnabledByDefault(): Boolean = true

    override fun buildVisitor(
        holder: ProblemsHolder,
        isOnTheFly: Boolean
    ): PsiElementVisitor {
        return object : PsiElementVisitor() {
            override fun visitElement(element: PsiElement) {
                if (element is Assignment) {
                    checkAssignment(element, holder)
                }
            }
        }
    }

    private fun checkAssignment(assignment: Assignment, holder: ProblemsHolder) {
        val arraySuffix: ArraySuffix? = assignment.arraySuffix
        val value: Value = assignment.value

        val arrayValue: Array? = value.array
        val hasArraySuffix = arraySuffix != null
        val hasArrayValue = arrayValue != null

        // Case 1: [] present, but value is not { ... }
        if (hasArraySuffix && !hasArrayValue) {
            val target = arraySuffix!!
            holder.registerProblem(
                target,
                "Array assignment '[]' must use '{ }' array initializer",
                ProblemHighlightType.GENERIC_ERROR_OR_WARNING
            )
        }

        // Case 2: { ... } value but no []
        if (!hasArraySuffix && hasArrayValue) {
            val target = arrayValue!!
            holder.registerProblem(
                target,
                "Array initializer '{ }' must be assigned to a '[]' array",
                ProblemHighlightType.GENERIC_ERROR_OR_WARNING
            )
        }
    }
}
