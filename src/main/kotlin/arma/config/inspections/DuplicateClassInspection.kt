package arma.config.inspections

import arma.config.psi.ClassBlock
import arma.config.psi.ClassBody
import arma.config.psi.Identifier
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil

@Suppress("InspectionDescriptionNotFoundInspection")
class DuplicateClassInspection : LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PsiElementVisitor() {
            override fun visitFile(psiFile: PsiFile) {
                checkContainer(psiFile, holder)
            }

            override fun visitElement(element: PsiElement) {
                if (element is ClassBody) checkContainer(element, holder)
            }
        }
    }

    private fun checkContainer(container: PsiElement, holder: ProblemsHolder) {
        val classBlocks = PsiTreeUtil.getChildrenOfTypeAsList(container, ClassBlock::class.java)
        if (classBlocks.isEmpty()) return
        val firstByName = mutableMapOf<String, PsiElement>()
        for (block in classBlocks) {
            val ident = PsiTreeUtil.getChildOfType(block, Identifier::class.java) ?: continue
            val name = ident.node.text.trim()
            if (name.isEmpty()) continue
            if (firstByName.putIfAbsent(name, block) != null) holder.registerProblem(ident, "Duplicate class '$name' in the same scope")
        }
    }
}
