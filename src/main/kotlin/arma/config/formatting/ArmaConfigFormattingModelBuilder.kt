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
                //  - First line elements (name[], operators, braces, commas) -> no extra indent
                //  - Values inside { ... } -> one extra indent
                ArmaConfigTypes.ARRAY_BLOCK -> {
                    when (elementType) {
                        // First line: identifier + [] + operators + braces + commas
                        ArmaConfigTypes.IDENTIFIER,
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

                // Keep math contents flat; they’re usually inline
                ArmaConfigTypes.MATH_BLOCK,
                ArmaConfigTypes.MATH_ELEMENT -> Indent.getNoneIndent()

                else -> Indent.getNoneIndent()
            }
        }

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

        override fun getSpacing(child1: Block?, child2: Block): Spacing? {
            if (child1 is ASTBlock && child2 is ASTBlock) {
                val leftNode = child1.node
                val rightNode = child2.node

                val leftType = leftNode?.elementType
                val rightType = rightNode?.elementType

                val leftParentType = leftNode?.treeParent?.elementType
                val rightParentType = rightNode?.treeParent?.elementType

                // Space between class name and class extension:
                // classBlock ::= CLASS_KEYWORD identifier classExtension? ...
                // so here we see IDENTIFIER (left) and CLASS_EXTENSION (right)
                if (leftType == ArmaConfigTypes.IDENTIFIER &&
                    rightType == ArmaConfigTypes.CLASS_EXTENSION &&
                    leftParentType == ArmaConfigTypes.CLASS_BLOCK &&
                    rightParentType == ArmaConfigTypes.CLASS_BLOCK
                ) {
                    return Spacing.createSpacing(
                        1, 1,   // exactly one space
                        0,      // no line feeds
                        false,
                        0
                    )
                }

                // Space between ':' and parent identifier → ": Parent"
                // COLON lives directly under CLASS_EXTENSION
                if (leftType == ArmaConfigTypes.COLON &&
                    leftParentType == ArmaConfigTypes.CLASS_EXTENSION
                ) {
                    return Spacing.createSpacing(
                        1, 1,
                        0,
                        false,
                        0
                    )
                }
            }

            // Everything else: default spacing
            return null
        }

        override fun isIncomplete(): Boolean = false
    }
}
