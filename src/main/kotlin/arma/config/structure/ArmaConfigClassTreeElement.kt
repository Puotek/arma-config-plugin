package arma.config.structure

import arma.config.psi.ClassBlock
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.structureView.impl.common.PsiTreeElementBase
import com.intellij.psi.util.PsiTreeUtil

class ArmaConfigClassTreeElement(element: ClassBlock) : PsiTreeElementBase<ClassBlock>(element) {

    override fun getPresentableText(): String {
        val classDecl = value ?: return "<class>"

        // New grammar: classBlock ::= CLASS_KEYWORD identifier classExtension? ...
        val ownName = classDecl.identifier
            ?.text
            ?.takeIf { it.isNotBlank() }
            ?: "<class>"

        // classExtension ::= COLON identifier
        val parentName = classDecl.classExtension
            ?.identifier
            ?.text
            ?.takeIf { it.isNotBlank() }

        return if (parentName != null) {
            "$ownName : $parentName"
        } else {
            ownName
        }
    }

    override fun getChildrenBase(): Collection<StructureViewTreeElement> {
        val classDecl = value ?: return emptyList()

        val body = classDecl.classBody ?: return emptyList()

        val nestedClasses = PsiTreeUtil.getChildrenOfTypeAsList(body, ClassBlock::class.java)
        return nestedClasses.map { ArmaConfigClassTreeElement(it) }
    }
}
