package arma.config.structure

import arma.config.psi.ArmaConfigFile
import arma.config.psi.ClassBlock
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.structureView.impl.common.PsiTreeElementBase
import com.intellij.psi.util.PsiTreeUtil

class ArmaConfigFileTreeElement(file: ArmaConfigFile) : PsiTreeElementBase<ArmaConfigFile>(file) {

    override fun getPresentableText(): String {
        val psiFile = value
        return psiFile?.name ?: "ArmaConfig"
    }

    override fun getChildrenBase(): Collection<StructureViewTreeElement> {
        val psiFile = value ?: return emptyList()
        val classDecls = PsiTreeUtil.getChildrenOfTypeAsList(psiFile, ClassBlock::class.java)
        return classDecls.map { ArmaConfigClassTreeElement(it) }
    }
}
