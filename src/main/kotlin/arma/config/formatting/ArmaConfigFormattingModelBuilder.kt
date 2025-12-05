package arma.config.formatting

import arma.config.psi.ArmaConfigTypes
import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.tree.IElementType

class ArmaConfigFormattingModelBuilder : FormattingModelBuilder {

    override fun createModel(formattingContext: FormattingContext): FormattingModel {
        val file = formattingContext.containingFile
        val settings = formattingContext.codeStyleSettings

        val rootBlock = ArmaConfigBlock(
            node = file.node,
            wrap = null,
            alignment = null,
            settings = settings
        )

        return FormattingModelProvider.createFormattingModelForPsiFile(file, rootBlock, settings)
    }

    private class ArmaConfigBlock(
        private val node: ASTNode,
        private val wrap: Wrap?,
        private val alignment: Alignment?,
        private val settings: CodeStyleSettings
    ) : ASTBlock {

        override fun getNode(): ASTNode = node
        override fun getTextRange(): TextRange = node.textRange
        override fun getWrap(): Wrap? = wrap
        override fun getAlignment(): Alignment? = alignment

        /**
         * Main indentation logic.
         */
        override fun getIndent(): Indent? {
            val parent = node.treeParent ?: return Indent.getNoneIndent()
            val parentType: IElementType = parent.elementType
            val elementType: IElementType = node.elementType

            // Never indent braces themselves
            if (elementType == ArmaConfigTypes.LBRACE ||
                elementType == ArmaConfigTypes.RBRACE
            ) return Indent.getNoneIndent()

            if (parent.psi is PsiFile) return Indent.getNoneIndent()

            return when (parentType) {
                // CLASS BODY: everything inside gets one indent,
                // except the braces which we handled above.
                ArmaConfigTypes.CLASS_BODY -> Indent.getNormalIndent()

                // Inside a parameter: keep it flat (name, '=', value, ';')
                ArmaConfigTypes.PARAMETER_BLOCK -> Indent.getNoneIndent()

                // ARRAY BLOCK:
                //  - First line elements (name[], +=, =, {, }; commas) -> no extra indent
                //  - Values inside { ... } -> one extra indent
                ArmaConfigTypes.ARRAY_BLOCK -> {
                    when (elementType) {
                        // First line: name[] + operators + braces + commas
                        ArmaConfigTypes.PARAMETER_NAME,
                        ArmaConfigTypes.LBRACKET,
                        ArmaConfigTypes.RBRACKET,
                        ArmaConfigTypes.EQUAL,
                        ArmaConfigTypes.PLUS,
                        ArmaConfigTypes.SEMICOLON,
                        ArmaConfigTypes.COMMA -> Indent.getNoneIndent()

                        // Values inside { ... } get one extra indent
                        ArmaConfigTypes.PARAMETER_VALUE -> Indent.getNormalIndent()

                        else -> Indent.getNoneIndent()
                    }
                }

                // Keep math and macro contents flat; they’re usually inline
                ArmaConfigTypes.MATH_BLOCK,
                ArmaConfigTypes.MATH_ELEMENT,
                ArmaConfigTypes.MACRO_BLOCK -> Indent.getNoneIndent()

                else -> Indent.getNoneIndent()
            }
        }

        /**
         * Indent when pressing Enter.
         */
        override fun getChildAttributes(newChildIndex: Int): ChildAttributes {
            return when (node.elementType) {
                // Inside a class body: indent one level
                ArmaConfigTypes.CLASS_BODY -> ChildAttributes(Indent.getNormalIndent(), null)


                // After '{' in an array: indent values one level
                ArmaConfigTypes.ARRAY_BLOCK -> ChildAttributes(Indent.getNormalIndent(), null)

                else -> ChildAttributes(Indent.getNoneIndent(), null)
            }
        }


        override fun isLeaf(): Boolean = node.firstChildNode == null

        private fun buildChildren(): MutableList<Block> {
            val result = mutableListOf<Block>()
            var child = node.firstChildNode

            while (child != null) {
                if (child.elementType != TokenType.WHITE_SPACE && child.textRange.length > 0) {
                    result.add(
                        ArmaConfigBlock(
                            node = child,
                            wrap = Wrap.createWrap(WrapType.NONE, false),
                            alignment = null,
                            settings = settings
                        )
                    )
                }
                child = child.treeNext
            }

            return result
        }

        override fun getSubBlocks(): MutableList<Block> = buildChildren()
        override fun getSpacing(child1: Block?, child2: Block): Spacing? = null
        override fun isIncomplete(): Boolean = false
    }
}
