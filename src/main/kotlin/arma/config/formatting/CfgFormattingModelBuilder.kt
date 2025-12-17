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
            val commonParentNode = leftNode!!.treeParent

            if (commonParent == CfgTypes.MACRO_BODY) return null

            when (leftType) {
                CfgTypes.LINE_COMMENT, CfgTypes.BLOCK_COMMENT, CfgTypes.PREPROCESSOR -> return null

                //todo move to commonParent checking and add respective settings for KEEP_EMPTY_ONELINE
                CfgTypes.LBRACE if rightType == CfgTypes.RBRACE -> return Spacing.createSpacing(
                    0, 0, 0, false, 0
                )
            }
            when (rightType) {
                CfgTypes.LINE_COMMENT, CfgTypes.BLOCK_COMMENT, CfgTypes.PREPROCESSOR -> return null
                CfgTypes.SEMICOLON -> return Spacing.createSpacing(
                    0, 0, 0, false, 0
                )
            }
            when (commonParent) {
                CfgTypes.CLASS_BLOCK -> {
                    if (leftType == CfgTypes.CLASS_KEYWORD) return Spacing.createSpacing(
                        1, 1, 0, false, 0
                    )
                    if (rightType == CfgTypes.CLASS_EXTENSION) {
                        val spaceBeforeColon = codeStyleSettings.CLASS_SPACE_COLON_BEFORE.toInt()
                        return Spacing.createSpacing(
                            spaceBeforeColon, spaceBeforeColon, 0, false, 0
                        )
                    }
                    if (rightType == CfgTypes.CLASS_BODY) {
                        val spaceBeforeBody = codeStyleSettings.CLASS_SPACE_BEFORE_BODY.toInt()
                        val wrap = codeStyleSettings.CLASS_WRAP(rightNode)
                        val newline = codeStyleSettings.CLASS_NEWLINE_BODY_OPEN
                        return Spacing.createSpacing(
                            spaceBeforeBody, spaceBeforeBody, (wrap && newline).toInt(), false, 0
                        )
                    }
                }

                CfgTypes.CLASS_EXTENSION -> {
                    val spaceAfterColon = codeStyleSettings.CLASS_SPACE_COLON_AFTER.toInt()
                    if (leftType == CfgTypes.COLON) return Spacing.createSpacing(
                        spaceAfterColon, spaceAfterColon, 0, false, 0
                    )
                }

                CfgTypes.CLASS_BODY -> {
                    val wrap = codeStyleSettings.CLASS_WRAP(commonParentNode)
                    val newLine = codeStyleSettings.CLASS_NEWLINE_BODY_CLOSE
                    val spaceWrapped = codeStyleSettings.CLASS_SPACE_WRAPPED.toInt()
                    //todo implement setting for collapse empty
//                    if (leftType == CfgTypes.LBRACE && rightType == CfgTypes.RBRACE)
                    if (leftType == CfgTypes.LBRACE) return Spacing.createSpacing(
                        spaceWrapped, spaceWrapped, wrap.toInt(), false, 0
                    )
                    if (rightType == CfgTypes.RBRACE) return Spacing.createSpacing(
                        spaceWrapped, spaceWrapped, (wrap && newLine).toInt(), false, 0
                    )
                }

                CfgTypes.ARRAY_BLOCK -> {
                    if (rightType == CfgTypes.LBRACKET ||
                        rightType == CfgTypes.RBRACKET ||
                        leftType == CfgTypes.PLUS) return Spacing.createSpacing(
                        0, 0, 0, false, 0
                    )
                    if (rightType == CfgTypes.EQUAL || rightType == CfgTypes.PLUS) {
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
                    val wrap = codeStyleSettings.ARRAYS_WRAP(commonParentNode)
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

                CfgTypes.DELETE_BLOCK -> if (leftType == CfgTypes.DELETE_KEYWORD) return Spacing.createSpacing(
                    1, 1, 0, false, 0
                )

            }
            return null
        }
    }
}
