package arma.config.inspections

import arma.config.psi.ArrayBlock
import arma.config.psi.ClassBlock
import arma.config.psi.Identifier
import arma.config.psi.ParameterBlock
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil

/**
 * Reports duplicate parameter/array assignments inside a single classBlock.
 *
 * It considers both:
 *  - normal parameters:   parameterBlock ::= identifier '=' ...
 *  - array parameters:    arrayBlock     ::= identifier '[]' '+='? '=' ...
 *
 * The key is the textual value of identifier, so macro-based names like
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

        val firstByName = mutableMapOf<String, PsiElement>()

        for (assignment in allAssignments) {
            val nameElement = findNameIdentifier(assignment) ?: continue
            val nameText = nameElement.text.trim()
            if (nameText.isEmpty()) continue

            val existing = firstByName[nameText]
            if (existing == null) {
                firstByName[nameText] = assignment
            } else {
                holder.registerProblem(
                    nameElement,
                    "Duplicate parameter '$nameText' in this class"
                )
            }
        }
    }

    /**
     * Returns the PSI element representing the identifier for either a ParameterBlock
     * or an ArrayBlock. This will be either:
     *  - a TEXT leaf-only identifier
     *  - or an identifier with macro/## inside.
     * We return the Identifier node so the whole thing is highlighted.
     */
    private fun findNameIdentifier(assignment: PsiElement): PsiElement? {
        return PsiTreeUtil.getChildOfType(assignment, Identifier::class.java)
    }
}
