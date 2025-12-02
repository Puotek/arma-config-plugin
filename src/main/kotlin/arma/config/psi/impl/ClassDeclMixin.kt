package arma.config.psi.impl

import arma.config.psi.ArmaConfigTypes
import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner

abstract class ClassDeclMixin(node: ASTNode) :
    ASTWrapperPsiElement(node), PsiNameIdentifierOwner {

    override fun getNameIdentifier(): PsiElement? =
        findChildByType(ArmaConfigTypes.IDENT)

    override fun getName(): String? =
        nameIdentifier?.text

    override fun setName(name: String): PsiElement {
        // Real renaming would replace the IDENT with a new one
        // For now, return `this` so renaming is a no-op but valid.
        return this
    }
}
