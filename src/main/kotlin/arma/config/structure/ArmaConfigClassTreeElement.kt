package arma.config.structure

import arma.config.psi.ClassDecl
import arma.config.psi.ClassIdent
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.structureView.impl.common.PsiTreeElementBase
import com.intellij.psi.util.PsiTreeUtil

class ArmaConfigClassTreeElement(element: ClassDecl) : PsiTreeElementBase<ClassDecl>(element) {

    override fun getPresentableText(): String {
        val classDecl = value ?: return "<class>"
        val ident: ClassIdent = classDecl.classIdent
        // classIdent may be IDENT or a macroInvocation — text is fine for both
        return ident.text ?: "<class>"
    }

    override fun getChildrenBase(): Collection<StructureViewTreeElement> {
        val classDecl = value ?: return emptyList()
        // Show nested classes as children
        val nested = PsiTreeUtil.getChildrenOfTypeAsList(classDecl, ClassDecl::class.java)
        return nested.map { ArmaConfigClassTreeElement(it) }
    }
}
