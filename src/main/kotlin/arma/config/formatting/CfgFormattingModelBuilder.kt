package arma.config.formatting

import arma.config.psi.CfgTypes
import com.intellij.formatting.*
import com.intellij.formatting.Wrap.createWrap
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType.WHITE_SPACE
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.rml.dfa.utils.toInt

class CfgFormattingModelBuilder : FormattingModelBuilder {

    override fun createModel(formattingContext: FormattingContext): FormattingModel {
        return FormattingModelProvider.createFormattingModelForPsiFile(
            formattingContext.containingFile,
            CfgBlock(
                formattingContext.containingFile.node,
                null,
                null,
                formattingContext.codeStyleSettings
            ),
            formattingContext.codeStyleSettings
        )
    }

    private class CfgBlock(
        private val node: ASTNode,
        private val wrap: Wrap?,
        private val alignment: Alignment?,
        private val settings: CodeStyleSettings
    ) : ASTBlock {

        private val codeStyleSettings: CfgCodeStyleSettings.Viewers
            get() = CfgCodeStyleSettings.Viewers(settings.getCustomSettings(CfgCodeStyleSettings::class.java))

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
                if (child.elementType != WHITE_SPACE && child.textRange.length > 0) result.add(
                    CfgBlock(
                        child,
                        createWrap(WrapType.NONE, false),
                        null,
                        settings
                    )
                )
                child = child.treeNext
            }
            return result
        }

        override fun getChildAttributes(newChildIndex: Int): ChildAttributes {
            return when (node.elementType) {
                CfgTypes.CLASS_BODY -> ChildAttributes(Indent.getNormalIndent(), null)
                CfgTypes.ARRAY_BODY -> ChildAttributes(Indent.getNormalIndent(), null)
                else -> ChildAttributes(Indent.getNoneIndent(), null)
            }
        } //fixme is this not useless?

        override fun getIndent(): Indent? {
            val parent = node.treeParent ?: return Indent.getNoneIndent()
            if (parent.psi is PsiFile) return Indent.getNoneIndent()
            when (node.elementType) {
                CfgTypes.LBRACE,
                CfgTypes.RBRACE -> return Indent.getNoneIndent()
            }
            when (parent.elementType) {
                CfgTypes.CLASS_BODY,
                CfgTypes.ARRAY_BODY -> return Indent.getNormalIndent()
            }
            return Indent.getNoneIndent()
        }

        override fun getSpacing(leftBlock: Block?, rightBlock: Block): Spacing? {
            if (leftBlock == null) return null
            if (leftBlock !is ASTBlock && rightBlock !is ASTBlock) return null
            leftBlock as ASTBlock
            rightBlock as ASTBlock

            val leftNode = leftBlock.node
            val rightNode = rightBlock.node

            val leftType = leftNode?.elementType
            val rightType = rightNode?.elementType

            val commonParent = leftNode?.treeParent?.elementType
            assert(commonParent == rightNode?.treeParent?.elementType)

            when (leftType) {
                CfgTypes.LINE_COMMENT, CfgTypes.BLOCK_COMMENT, CfgTypes.PREPROCESSOR -> return null
                CfgTypes.LBRACE if rightType == CfgTypes.RBRACE -> return Spacing.createSpacing(
                    0, 0, 0, false, 0
                )
            }
            when (rightType) {
                CfgTypes.LINE_COMMENT, CfgTypes.BLOCK_COMMENT, CfgTypes.PREPROCESSOR -> return null
            }
            when (commonParent) {
                CfgTypes.CLASS_BODY -> return when (leftType) {
                    CfgTypes.IDENTIFIER if rightType == CfgTypes.SEMICOLON -> Spacing.createSpacing(
                        0, 0, 0, false, 0
                    )

                    else -> Spacing.createSpacing(
                        1, 1, 1, true, 3
                    )
                }

                CfgTypes.ARRAY_BLOCK -> {
                    if (leftType == CfgTypes.ARRAY_BODY) {
                        return Spacing.createSpacing(
                            0, 0, 0, false, 0
                        )
                    }
                    if (rightType == CfgTypes.EQUAL) {
                        val spaceEqualBefore = codeStyleSettings.ARRAYS_SPACE_EQUALS_BEFORE
                        return Spacing.createSpacing(
                            spaceEqualBefore, spaceEqualBefore, 0, false, 0
                        )
                    }
                    if (rightType == CfgTypes.ARRAY_BODY) {
                        val spaceEqualAfter = codeStyleSettings.ARRAYS_SPACE_EQUALS_AFTER
                        val wrap = codeStyleSettings.ARRAYS_WRAP(rightNode)
                        val newline = codeStyleSettings.ARRAYS_NEWLINE_BODY_OPEN
                        return Spacing.createSpacing(
                            spaceEqualAfter, spaceEqualAfter, (wrap && newline).toInt(), false, 0
                        )
                    }
                }

                CfgTypes.ARRAY_BODY -> {
                    val wrap = codeStyleSettings.ARRAYS_WRAP(leftNode.treeParent)
                    val leadingCommas = codeStyleSettings.ARRAYS_LEADING_COMMAS
                    val newLine = codeStyleSettings.ARRAYS_NEWLINE_BODY_CLOSE
                    if (leftType == CfgTypes.LBRACE) return Spacing.createSpacing(
                        0, 0, wrap.toInt(), false, 0
                    )
                    if (rightType == CfgTypes.RBRACE) {
                        return Spacing.createSpacing(
                            0, 0, (wrap && newLine).toInt(), false, 0
                        )
                    }
                    if (rightType == CfgTypes.COMMA) return Spacing.createSpacing(
                        0, 0, (wrap && leadingCommas).toInt(), false, 0
                    )
                    if (leftType == CfgTypes.COMMA) return Spacing.createSpacing(
                        (!(wrap && leadingCommas)).toInt(), (!(wrap && leadingCommas)).toInt(), (wrap && !leadingCommas).toInt(), false, 0
                    )
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
            return null
        }
    }
}
