package arma.config.structure

import arma.config.psi.ArmaConfigFile
import arma.config.psi.ClassDecl
import com.intellij.ide.structureView.StructureViewModel
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.structureView.TextEditorBasedStructureViewModel
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement

class ArmaConfigStructureViewModel(
    file: ArmaConfigFile, editor: Editor?
) : TextEditorBasedStructureViewModel(editor, file), StructureViewModel.ElementInfoProvider {

    private val rootElement = ArmaConfigFileTreeElement(file)

    override fun getRoot(): StructureViewTreeElement = rootElement

    override fun getSuitableClasses(): Array<Class<out PsiElement>> = arrayOf(ClassDecl::class.java)

    override fun isAlwaysShowsPlus(element: StructureViewTreeElement?): Boolean = false

    override fun isAlwaysLeaf(element: StructureViewTreeElement?): Boolean {
        // Classes can contain nested classes, so only leaf if no children
        val children = element?.children ?: StructureViewTreeElement.EMPTY_ARRAY
        return children.isEmpty()
    }
}
