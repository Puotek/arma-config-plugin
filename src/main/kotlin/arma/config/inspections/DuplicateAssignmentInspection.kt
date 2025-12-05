package arma.config.inspections

import arma.config.psi.ArrayBlock
import arma.config.psi.ClassBlock
import arma.config.psi.ParameterBlock
import arma.config.psi.ParameterName
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil

/**
 * Reports duplicate parameter/array assignments inside a single classBlock.
 *
 * It considers both:
 *  - normal parameters:   parameterBlock ::= parameterName '=' ...
 *  - array parameters:    arrayBlock     ::= parameterName '[]' '+='? '=' ...
 *
 * The key is the textual value of parameterName, so macro-based names like
 *   TAG(value)
 * are also grouped and checked for duplicates.
 */
@Suppress("InspectionDescriptionNotFoundInspection")
class DuplicateAssignmentInspection : LocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PsiElementVisitor() {

            override fun visitElement(element: PsiElement) {
                if (element is ClassBlock) {
                    checkClass(element, holder)
                }
            }
        }
    }

    private fun checkClass(klass: ClassBlock, holder: ProblemsHolder) {
        // Direct child parameter & array blocks of this class
        val parameterBlocks = PsiTreeUtil.getChildrenOfTypeAsList(klass, ParameterBlock::class.java)
        val arrayBlocks = PsiTreeUtil.getChildrenOfTypeAsList(klass, ArrayBlock::class.java)

        val allAssignments: List<PsiElement> = buildList(parameterBlocks.size + arrayBlocks.size) {
            addAll(parameterBlocks)
            addAll(arrayBlocks)
        }
        if (allAssignments.isEmpty()) return

        // Map from parameter name text -> first seen assignment PSI
        val firstByName = mutableMapOf<String, PsiElement>()

        for (assignment in allAssignments) {
            val nameElement = findParameterNameElement(assignment) ?: continue
            val nameText = nameElement.text.trim()
            if (nameText.isEmpty()) continue

            val existing = firstByName[nameText]
            if (existing == null) {
                firstByName[nameText] = assignment
            } else {
                // Duplicate: register the problem on the *name* node (TEXT or macroBlock)
                holder.registerProblem(
                    nameElement,
                    "Duplicate parameter '$nameText' in this class"
                )
            }
        }
    }

    /**
     * Returns the PSI element representing the parameterName for either a ParameterBlock
     * or an ArrayBlock. This will be either:
     *  - a TEXT leaf
     *  - or a MacroBlock subtree (when parameterName ::= macroBlock)
     * We return the ParameterName node itself so the whole thing is highlighted.
     */
    private fun findParameterNameElement(assignment: PsiElement): PsiElement? {
        val paramName = PsiTreeUtil.getChildOfType(assignment, ParameterName::class.java)
        return paramName
    }
}
