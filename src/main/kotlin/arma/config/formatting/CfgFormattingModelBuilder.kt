package arma.config.formatting

import arma.config.psi.CfgTypes
import com.intellij.formatting.*
import com.intellij.formatting.Wrap.createWrap
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType.WHITE_SPACE
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.tree.IElementType

class CfgFormattingModelBuilder : FormattingModelBuilder {

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

        private val styleSettings: CfgCodeStyleSettings
            get() = settings.getCustomSettings(CfgCodeStyleSettings::class.java)

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
                CfgTypes.CLASS_BODY -> ChildAttributes(Indent.getNormalIndent(), null)

                // After '=' in an arrayBlock: indent the arrayBody one level
                CfgTypes.ARRAY_BLOCK -> ChildAttributes(Indent.getNormalIndent(), null)

                // Inside an arrayBody: indent elements one level inside '{ }'
                CfgTypes.ARRAY_BODY -> ChildAttributes(Indent.getNormalIndent(), null)

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
                CfgTypes.LBRACE,
                CfgTypes.RBRACE -> return Indent.getNoneIndent()
            }


            return when (parentType) {
                // CLASS BODY: everything inside gets one indent,
                // except the braces which we handled above.
                CfgTypes.CLASS_BODY -> Indent.getNormalIndent()

                // Inside a parameter: keep it flat (name, '=', value, ';')
                CfgTypes.PARAMETER_BLOCK -> Indent.getNoneIndent()

                // ARRAY BLOCK:
                //  - identifier/[]/operators stay at base indent
                //  - ARRAY_BODY as a whole is indented one level (if on separate line)
                CfgTypes.ARRAY_BLOCK -> {
                    when (elementType) {
                        CfgTypes.IDENTIFIER,
                        CfgTypes.LBRACKET,
                        CfgTypes.RBRACKET,
                        CfgTypes.EQUAL,
                        CfgTypes.PLUS,
                        CfgTypes.SEMICOLON,
                        CfgTypes.COMMA -> Indent.getNoneIndent()

                        CfgTypes.ARRAY_BODY -> Indent.getNormalIndent()

                        else -> Indent.getNoneIndent()
                    }
                }

                // Inside arrayBody:
                //  - values (PARAMETER_VALUE) get one indent inside '{ }'
                //  - nested ARRAY_BODY (nested arrays) also get one indent
                //  - commas and braces stay at base indent
                CfgTypes.ARRAY_BODY -> {
                    when (elementType) {
                        CfgTypes.PARAMETER_VALUE,
                        CfgTypes.ARRAY_BODY -> Indent.getNormalIndent()

                        else -> Indent.getNoneIndent()
                    }
                }

                // Keep math contents flat; they’re usually inline
                CfgTypes.MATH_BLOCK,
                CfgTypes.MATH_ELEMENT -> Indent.getNoneIndent()

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
                    CfgTypes.CLASS_BODY -> {
                        return when (leftType) {
                            //collapse empty body
                            CfgTypes.LBRACE if rightType == CfgTypes.RBRACE -> Spacing.createSpacing(
                                0, 0, 0, false, 0
                            )
                            //for `identifier SEMICOLON` we don't make newline
                            CfgTypes.IDENTIFIER if rightType == CfgTypes.SEMICOLON -> Spacing.createSpacing(
                                0, 0, 0, false, 0
                            )
                            //for anything else we make a newline
                            else -> Spacing.createSpacing(
                                1, 1, 1, false, 0
                            )
                        }
                    }

                    CfgTypes.ARRAY_BODY -> {
                        //collapse empty body {}
                        if (leftType == CfgTypes.LBRACE && rightType == CfgTypes.RBRACE) return Spacing.createSpacing(
                            0, 0, 0, false, 0
                        )



                        when (
                            //check current settings
                            when (styleSettings.WRAP_ARRAYS) {
                                CfgCodeStyleSettings.ArrayLayoutMode.ONELINE.ordinal -> BodyLayout.ONELINE
                                CfgCodeStyleSettings.ArrayLayoutMode.MULTILINE.ordinal -> BodyLayout.MULTILINE
                                CfgCodeStyleSettings.ArrayLayoutMode.SMART.ordinal
                                    -> leftNode?.treeParent?.let<ASTNode, BodyLayout>(::detectBodyLayout)
                                else -> BodyLayout.MULTILINE
                            }!!
                        ) {
                            BodyLayout.ONELINE -> {
                                //remove newline after and before {} when array has content
                                if (leftType == CfgTypes.LBRACE || rightType == CfgTypes.RBRACE) return Spacing.createSpacing(
                                    0, 0, 0, false, 0
                                )
                                //no space or newline before comma
                                if (rightType == CfgTypes.COMMA) return Spacing.createSpacing(
                                    0, 0, 0, false, 0
                                )
                                //space and no newline after comma
                                if (leftType == CfgTypes.COMMA) return Spacing.createSpacing(
                                    1, 1, 0, false, 0
                                )
                            }

                            BodyLayout.MULTILINE -> {
                                //newline after and before {} when array has content
                                if (leftType == CfgTypes.LBRACE || rightType == CfgTypes.RBRACE) return Spacing.createSpacing(
                                    0, 0, 1, false, 0
                                )
                                //no space or newline before comma
                                if (rightType == CfgTypes.COMMA) return Spacing.createSpacing(
                                    0, 0, 0, false, 0
                                )
                                //space and newline after comma
                                if (leftType == CfgTypes.COMMA) return Spacing.createSpacing(
                                    1, 1, 1, false, 0
                                )
                            }
                        }
                    }

                    CfgTypes.CLASS_BLOCK -> {
                        //space after `class`
                        if (leftType == CfgTypes.CLASS_KEYWORD) return Spacing.createSpacing(
                            1, 1, 0, false, 0
                        )
                        //space before body
                        if (rightType == CfgTypes.CLASS_BODY) return Spacing.createSpacing(
                            1, 1, 0, false, 0
                        )
                        //no space before `;`
                        if (rightType == CfgTypes.SEMICOLON) return Spacing.createSpacing(
                            0, 0, 0, false, 0
                        )
                        //space before `:`
                        if (rightType == CfgTypes.CLASS_EXTENSION) return Spacing.createSpacing(
                            1, 1, 0, false, 0
                        )
                    }

                    CfgTypes.DELETE_BLOCK -> {
                        //space after `class`
                        if (leftType == CfgTypes.DELETE_KEYWORD) return Spacing.createSpacing(
                            1, 1, 0, false, 0
                        )
                        //no space before `;`
                        if (rightType == CfgTypes.SEMICOLON) return Spacing.createSpacing(
                            0, 0, 0, false, 0
                        )
                    }

                    CfgTypes.CLASS_EXTENSION -> {
                        if (leftType == CfgTypes.COLON) return Spacing.createSpacing(
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
                CfgTypes.LINE_COMMENT,
                CfgTypes.BLOCK_COMMENT
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
