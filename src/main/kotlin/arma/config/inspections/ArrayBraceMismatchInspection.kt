package arma.config.inspections

import arma.config.psi.Array
import arma.config.psi.Assignment
import arma.config.psi.PreprocValue
import arma.config.psi.Value
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil

/**
 * Checks that array assignments use matching syntax:
 *
 *   name[] = { ... };   // OK
 *   name   = value;     // OK, value is not { ... }
 *
 * and flags:
 *
 *   name[] = 1;         // [] but no { }
 *   name   = { 1, 2 };  // { } but no []
 *
 * Because of the grammar, a "{ }" initializer may be parsed either as an Array
 * or as a PreprocValue containing "{...}". We treat both as "array-like".
 */
class ArrayBraceMismatchInspection : LocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : arma.config.psi.Visitor() {

            override fun visitAssignment(o: Assignment) {
                val arraySuffix = o.arraySuffix              // [] on the left?
                val value: Value = o.value

                val hasBrackets = arraySuffix != null

                // 1) Try to see if RHS is a real Array node (array ::= '{' valueList? '}' )
                val arrayValue: Array? = value.array ?: PsiTreeUtil.getChildOfType(value, Array::class.java)

                // 2) If not, check if it's a PreprocValue that *looks like* a braces initializer
                val preprocValue: PreprocValue? =
                    value.preprocValue ?: PsiTreeUtil.getChildOfType(value, PreprocValue::class.java)

                val arrayLikeElement: PsiElement? = when {
                    arrayValue != null -> arrayValue
                    preprocValue != null && isBracesLike(preprocValue) -> preprocValue
                    else -> null
                }

                val hasBracesArray = arrayLikeElement != null

                // Case 1: name[] = <not-array>;
                if (hasBrackets && !hasBracesArray) {
                    val nameElement = o.firstChild  // IDENT is the first child in assignment
                    if (nameElement != null) {
                        holder.registerProblem(
                            nameElement,
                            "Array property '[]' must use '{ }' array value",
                            ProblemHighlightType.GENERIC_ERROR_OR_WARNING
                        )
                    }
                    return
                }

                // Case 2: name = { ... };
                if (!hasBrackets && hasBracesArray) {
                    holder.registerProblem(
                        arrayLikeElement,
                        "Array value '{ }' requires '[]' array property suffix",
                        ProblemHighlightType.GENERIC_ERROR_OR_WARNING
                    )
                }
            }
        }
    }

    /**
     * Very small heuristic: treat a PreprocValue as "array-like" if its
     * trimmed text begins with '{' and ends with '}'.
     */
    private fun isBracesLike(preprocValue: PreprocValue): Boolean {
        val text = preprocValue.text.trim()
        return text.length >= 2 && text.first() == '{' && text.last() == '}'
    }
}
