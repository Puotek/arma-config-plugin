package arma.config.folding

import arma.config.psi.ArmaConfigTypes
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
class ArmaConfigFoldingBuilder : FoldingBuilderEx(), DumbAware {

    // Called to collect fold regions for the file
    override fun buildFoldRegions(
        root: PsiElement,  // Root PSI element (file)
        document: Document,
        quick: Boolean
    ): Array<FoldingDescriptor> {

        // Will accumulate all folding regions here
        val descriptors = mutableListOf<FoldingDescriptor>()

        // Recursively walk the PSI tree
        root.accept(object : PsiRecursiveElementWalkingVisitor() {
            override fun visitElement(element: PsiElement) {
                // First, recurse into children
                super.visitElement(element)

                // Check the element type
                val type = element.node.elementType
                when (type) {
                    // For class declarations and arrays, try to create a folding region
                    ArmaConfigTypes.CLASS_DECL,
                    ArmaConfigTypes.ARRAY -> addBraceBlockFolding(element, descriptors)
                }
            }
        })

        // Return a fixed array of descriptors
        return descriptors.toTypedArray()
    }

    /**
     * Creates a folding region for the contents between `{` and `}` in `element`.
     */
    private fun addBraceBlockFolding(
        element: PsiElement,
        result: MutableList<FoldingDescriptor>
    ) {
        val node = element.node

        // Look for left and right brace tokens as direct children
        val lbrace = node.findChildByType(ArmaConfigTypes.LBRACE)
        val rbrace = node.findChildByType(ArmaConfigTypes.RBRACE)

        // Only create a folding region if both braces exist
        if (lbrace != null && rbrace != null) {
            // We want to fold the *inside* of the braces:
            // from after '{' to before '}'
            val start = lbrace.textRange.endOffset   // position right after '{'
            val end = rbrace.textRange.startOffset   // position right before '}'

            // Only add a folding if there is at least one character inside
            if (end > start) {
                // FoldingDescriptor tells IDEA: "this node, range [start, end) is foldable"
                result.add(FoldingDescriptor(node, TextRange(start, end)))
            }
        }
    }

    // Text shown when folded
    override fun getPlaceholderText(node: ASTNode): String =
        when (node.elementType) {
            ArmaConfigTypes.CLASS_DECL -> "{ ... }"
            ArmaConfigTypes.ARRAY      -> "{ ... }"
            else                       -> "..."
        }

    // Whether regions are collapsed by default (we say no)
    override fun isCollapsedByDefault(node: ASTNode): Boolean = false
}
