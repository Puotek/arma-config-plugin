package arma.config.formatting

import arma.config.psi.ArmaConfigTypes
import com.intellij.formatting.*
import com.intellij.formatting.Wrap.createWrap
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType.WHITE_SPACE
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

        private val styleSettings: ArmaConfigCodeStyleSettings
            get() = settings.getCustomSettings(ArmaConfigCodeStyleSettings::class.java)

        override fun getNode(): ASTNode = node
        override fun getTextRange(): TextRange = node.textRange
        override fun getWrap(): Wrap? = wrap
        override fun getAlignment(): Alignment? = alignment
        override fun isIncomplete(): Boolean = false
        override fun isLeaf(): Boolean = node.firstChildNode == null
        override fun getSubBlocks(): MutableList<Block> {
            val result = mutableListOf<Block>()
            var child = this.node.firstChildNode
            while (child != null) {
                if (child.elementType != WHITE_SPACE && child.textRange.length > 0) {
                    result.add(
                        ArmaConfigBlock(
                            node = child,
                            wrap = createWrap(WrapType.NONE, false),
                            alignment = null,
                            settings = settings
                        )
                    )
                }
                child = child.treeNext
            }
            return result
        }

        override fun getChildAttributes(newChildIndex: Int): ChildAttributes {
            return when (node.elementType) {
                // Inside a class body: indent one level
                ArmaConfigTypes.CLASS_BODY -> ChildAttributes(Indent.getNormalIndent(), null)

                // After '=' in an arrayBlock: indent the arrayBody one level
                ArmaConfigTypes.ARRAY_BLOCK -> ChildAttributes(Indent.getNormalIndent(), null)

                // Inside an arrayBody: indent elements one level inside '{ }'
                ArmaConfigTypes.ARRAY_BODY -> ChildAttributes(Indent.getNormalIndent(), null)

                else -> ChildAttributes(Indent.getNoneIndent(), null)
            }
        }

        override fun getIndent(): Indent? {
            val parent = node.treeParent ?: return Indent.getNoneIndent()
            val parentType: IElementType = parent.elementType
            val elementType: IElementType = node.elementType

            // Top-level file contents are never indented
            if (parent.psi is PsiFile) return Indent.getNoneIndent()

            // Never indent braces themselves
            when (elementType) {
                ArmaConfigTypes.LBRACE,
                ArmaConfigTypes.RBRACE -> return Indent.getNoneIndent()
            }


            return when (parentType) {
                // CLASS BODY: everything inside gets one indent,
                // except the braces which we handled above.
                ArmaConfigTypes.CLASS_BODY -> Indent.getNormalIndent()

                // Inside a parameter: keep it flat (name, '=', value, ';')
                ArmaConfigTypes.PARAMETER_BLOCK -> Indent.getNoneIndent()

                // ARRAY BLOCK:
                //  - identifier/[]/operators stay at base indent
                //  - ARRAY_BODY as a whole is indented one level (if on separate line)
                ArmaConfigTypes.ARRAY_BLOCK -> {
                    when (elementType) {
                        ArmaConfigTypes.IDENTIFIER,
                        ArmaConfigTypes.LBRACKET,
                        ArmaConfigTypes.RBRACKET,
                        ArmaConfigTypes.EQUAL,
                        ArmaConfigTypes.PLUS,
                        ArmaConfigTypes.SEMICOLON,
                        ArmaConfigTypes.COMMA -> Indent.getNoneIndent()

                        ArmaConfigTypes.ARRAY_BODY -> Indent.getNormalIndent()

                        else -> Indent.getNoneIndent()
                    }
                }

                // Inside arrayBody:
                //  - values (PARAMETER_VALUE) get one indent inside '{ }'
                //  - nested ARRAY_BODY (nested arrays) also get one indent
                //  - commas and braces stay at base indent
                ArmaConfigTypes.ARRAY_BODY -> {
                    when (elementType) {
                        ArmaConfigTypes.PARAMETER_VALUE,
                        ArmaConfigTypes.ARRAY_BODY -> Indent.getNormalIndent()

                        else -> Indent.getNoneIndent()
                    }
                }

                // Keep math contents flat; they’re usually inline
                ArmaConfigTypes.MATH_BLOCK,
                ArmaConfigTypes.MATH_ELEMENT -> Indent.getNoneIndent()

                else -> Indent.getNoneIndent()
            }
        }

        override fun getSpacing(child1: Block?, child2: Block): Spacing? {
            if (child1 is ASTBlock && child2 is ASTBlock) {
                val leftNode = child1.node
                val rightNode = child2.node

                val leftType = leftNode?.elementType
                val rightType = rightNode?.elementType

                val leftParentType = leftNode?.treeParent?.elementType
                val rightParentType = rightNode?.treeParent?.elementType
                val commonParent = if (leftParentType == rightParentType) leftParentType else null

                when (commonParent) {
                    ArmaConfigTypes.CLASS_BODY -> {
                        return when (leftType) {
                            //collapse empty body
                            ArmaConfigTypes.LBRACE if rightType == ArmaConfigTypes.RBRACE -> Spacing.createSpacing(
                                0, 0, 0, false, 0
                            )
                            //for `identifier SEMICOLON` we don't make newline
                            ArmaConfigTypes.IDENTIFIER if rightType == ArmaConfigTypes.SEMICOLON -> Spacing.createSpacing(
                                0, 0, 0, false, 0
                            )
                            //for anything else we make a newline
                            else -> Spacing.createSpacing(
                                1, 1, 1, false, 0
                            )
                        }
                    }

                    ArmaConfigTypes.ARRAY_BODY -> {
                        //collapse empty body {}
                        if (leftType == ArmaConfigTypes.LBRACE && rightType == ArmaConfigTypes.RBRACE) return Spacing.createSpacing(
                            0, 0, 0, false, 0
                        )



                        when (
                            //check current settings
                            when (styleSettings.WRAP_ARRAYS) {
                                ArmaConfigCodeStyleSettings.ArrayLayoutMode.ONELINE.ordinal -> BodyLayout.ONELINE
                                ArmaConfigCodeStyleSettings.ArrayLayoutMode.MULTILINE.ordinal -> BodyLayout.MULTILINE
                                ArmaConfigCodeStyleSettings.ArrayLayoutMode.SMART.ordinal
                                    -> leftNode?.treeParent?.let<ASTNode, BodyLayout>(::detectBodyLayout)
                                else -> BodyLayout.MULTILINE
                            }!!
                        ) {
                            BodyLayout.ONELINE -> {
                                //remove newline after and before {} when array has content
                                if (leftType == ArmaConfigTypes.LBRACE || rightType == ArmaConfigTypes.RBRACE) return Spacing.createSpacing(
                                    0, 0, 0, false, 0
                                )
                                //no space or newline before comma
                                if (rightType == ArmaConfigTypes.COMMA) return Spacing.createSpacing(
                                    0, 0, 0, false, 0
                                )
                                //space and no newline after comma
                                if (leftType == ArmaConfigTypes.COMMA) return Spacing.createSpacing(
                                    1, 1, 0, false, 0
                                )
                            }

                            BodyLayout.MULTILINE -> {
                                //newline after and before {} when array has content
                                if (leftType == ArmaConfigTypes.LBRACE || rightType == ArmaConfigTypes.RBRACE) return Spacing.createSpacing(
                                    0, 0, 1, false, 0
                                )
                                //no space or newline before comma
                                if (rightType == ArmaConfigTypes.COMMA) return Spacing.createSpacing(
                                    0, 0, 0, false, 0
                                )
                                //space and newline after comma
                                if (leftType == ArmaConfigTypes.COMMA) return Spacing.createSpacing(
                                    1, 1, 1, false, 0
                                )
                            }
                        }
                    }

                    ArmaConfigTypes.CLASS_BLOCK -> {
                        //space after `class`
                        if (leftType == ArmaConfigTypes.CLASS_KEYWORD) return Spacing.createSpacing(
                            1, 1, 0, false, 0
                        )
                        //space before body
                        if (rightType == ArmaConfigTypes.CLASS_BODY) return Spacing.createSpacing(
                            1, 1, 0, false, 0
                        )
                        //no space before `;`
                        if (rightType == ArmaConfigTypes.SEMICOLON) return Spacing.createSpacing(
                            0, 0, 0, false, 0
                        )
                        //space before `:`
                        if (rightType == ArmaConfigTypes.CLASS_EXTENSION) return Spacing.createSpacing(
                            1, 1, 0, false, 0
                        )
                    }

                    ArmaConfigTypes.DELETE_BLOCK -> {
                        //space after `class`
                        if (leftType == ArmaConfigTypes.DELETE_KEYWORD) return Spacing.createSpacing(
                            1, 1, 0, false, 0
                        )
                        //no space before `;`
                        if (rightType == ArmaConfigTypes.SEMICOLON) return Spacing.createSpacing(
                            0, 0, 0, false, 0
                        )
                    }

                    ArmaConfigTypes.CLASS_EXTENSION -> {
                        if (leftType == ArmaConfigTypes.COLON) return Spacing.createSpacing(
                            1, 1, 0, false, 0
                        )
                    }
                }
            }
            // otherwise dont touch spacing
            return null
        }

        private enum class BodyLayout {
            ONELINE,
            MULTILINE
        }

        private fun detectBodyLayout(body: ASTNode): BodyLayout {
            // Check if first real element of a body/block is on newline or same line as block opening

            // Grab two first valid elements
            var firstChild: ASTNode? = null
            var secondChild: ASTNode? = null
            val ignoredTypes = arrayOf(
                WHITE_SPACE,
                ArmaConfigTypes.LINE_COMMENT,
                ArmaConfigTypes.BLOCK_COMMENT
            )
            var child = body.firstChildNode
            while (child != null) {
                if (child.elementType !in ignoredTypes) if (firstChild == null) firstChild = child else {
                    secondChild = child
                    break
                }
                child = child.treeNext
            }

            // No elements → doesn't matter, treat as MULTILINE by default
            if (firstChild == null || secondChild == null) return BodyLayout.MULTILINE

            // If there is a newline between '{' and first element → multiline layout
            return if (body.psi.containingFile.text.subSequence(
                    firstChild.textRange.endOffset,
                    secondChild.textRange.startOffset
                ).any { it == '\n' }
            ) BodyLayout.MULTILINE else BodyLayout.ONELINE
        }
    }
}
