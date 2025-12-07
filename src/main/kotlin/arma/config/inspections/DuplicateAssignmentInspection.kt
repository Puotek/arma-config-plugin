package arma.config.inspections

import arma.config.psi.ArrayBlock
import arma.config.psi.ClassBlock
import arma.config.psi.ClassBody
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
                if (element is ClassBody) checkClass(element, holder)
            }
        }
    }

    private fun checkClass(classBlock: ClassBody, holder: ProblemsHolder) {
        // Direct child parameter & array blocks of this class
        val parameterBlocks = PsiTreeUtil.getChildrenOfTypeAsList(classBlock, ParameterBlock::class.java)
        if (parameterBlocks.isNotEmpty()) {
            val firstByName = mutableMapOf<String, PsiElement>()
            for (block in parameterBlocks) {
                val ident = block.firstChild.node.text.trim()
                if (ident.isEmpty()) continue
                if (firstByName.containsKey(ident)) holder.registerProblem(block.firstChild, "Duplicate parameter '$ident' in class")
                else firstByName[ident] = block
            }
        }
        val arrayBlocks = PsiTreeUtil.getChildrenOfTypeAsList(classBlock, ArrayBlock::class.java)
        if (arrayBlocks.isNotEmpty()) {
            val firstByName = mutableMapOf<String, PsiElement>()
            for (block in arrayBlocks) {
                val ident = block.firstChild.node.text.trim()
                if (ident.isEmpty()) continue
                if (firstByName.containsKey(ident)) holder.registerProblem(block.firstChild, "Duplicate array parameter '$ident' in class")
                else firstByName[ident] = block
            }
        }
    }
}
