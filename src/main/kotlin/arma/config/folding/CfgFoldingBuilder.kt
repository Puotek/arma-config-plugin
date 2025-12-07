package arma.config.folding

import arma.config.psi.CfgTypes
import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiRecursiveElementWalkingVisitor

/**
 * Provides code folding regions (collapsible sections) for Arma config.
 *
 * Two kinds of folding:
 *  - class bodies: class Foo { ... }
 *  - array bodies: { a, b, c }
 */
class CfgFoldingBuilder : FoldingBuilderEx(), DumbAware {

    override fun buildFoldRegions(
        root: PsiElement, document: Document, quick: Boolean
    ): Array<FoldingDescriptor> {

        val descriptors = mutableListOf<FoldingDescriptor>()

        // Recursively walk the PSI tree
        root.accept(object : PsiRecursiveElementWalkingVisitor() {
            override fun visitElement(element: PsiElement) {
                super.visitElement(element)

                val type = element.node.elementType
                when (type) {
                    // For class declarations and arrays, try to create a folding region
                    CfgTypes.CLASS_BLOCK, CfgTypes.ARRAY_BLOCK -> addBraceBlockFolding(
                        element,
                        descriptors
                    )
                }
            }
        })

        return descriptors.toTypedArray()
    }

    /**
     * Creates a folding region for the contents between `{` and `}` in `element`.
     */
    private fun addBraceBlockFolding(
        element: PsiElement, result: MutableList<FoldingDescriptor>
    ) {
        val node = element.node

        // Look for left and right brace tokens as direct children
        val lbrace = node.findChildByType(CfgTypes.LBRACE)
        val rbrace = node.findChildByType(CfgTypes.RBRACE)

        // Only create a folding region if both braces exist
        if (lbrace != null && rbrace != null) {
            // Fold INCLUDING the braces:
            // from the '{' itself to the '}' itself
            val start = lbrace.textRange.startOffset
            val end = rbrace.textRange.endOffset

            if (end > start) {
                result.add(FoldingDescriptor(node, TextRange(start, end)))
            }
        }
    }

    override fun getPlaceholderText(node: ASTNode): String = when (node.elementType) {
        CfgTypes.CLASS_BLOCK -> "{...}"
        CfgTypes.ARRAY_BLOCK -> "{...}"
        else -> "..."
    }

    override fun isCollapsedByDefault(node: ASTNode): Boolean = false
}
