package arma.config.structure

import arma.config.psi.ClassBlock
import arma.config.psi.ClassName
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.structureView.impl.common.PsiTreeElementBase
import com.intellij.psi.util.PsiTreeUtil

class ArmaConfigClassTreeElement(element: ClassBlock) : PsiTreeElementBase<ClassBlock>(element) {

    override fun getPresentableText(): String {
        val classDecl = value ?: return "<class>"
        val ident: ClassName? = classDecl.className
        // className may be TEXT or a macroBlock – text is fine for both
        return ident?.text ?: "<class>"
    }

    override fun getChildrenBase(): Collection<StructureViewTreeElement> {
        val classDecl = value ?: return emptyList()

        // Nested classes live inside the class body, not as direct children of ClassBlock
        val body = classDecl.classBody ?: return emptyList()

        // Only direct nested classes of this body; deeper ones are handled recursively
        val nestedClasses = PsiTreeUtil.getChildrenOfTypeAsList(body, ClassBlock::class.java)

        return nestedClasses.map { ArmaConfigClassTreeElement(it) }
    }
}
