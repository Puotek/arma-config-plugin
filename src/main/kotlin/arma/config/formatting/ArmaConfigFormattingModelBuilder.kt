package arma.config.formatting

import arma.config.ArmaConfigLanguage
import arma.config.psi.ArmaConfigTypes
import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.formatter.common.AbstractBlock

class ArmaConfigFormattingModelBuilder : FormattingModelBuilder {

    override fun createModel(formattingContext: FormattingContext): FormattingModel {
        val settings = formattingContext.codeStyleSettings
        val spacingBuilder = createSpacingBuilder(settings)

        val rootBlock = ArmaConfigBlock(
            node = formattingContext.node, wrap = null, alignment = null, spacingBuilder = spacingBuilder
        )

        return FormattingModelProvider.createFormattingModelForPsiFile(
            formattingContext.containingFile, rootBlock, settings
        )
    }

    override fun getRangeAffectingIndent(
        file: PsiFile, offset: Int, nodeAtOffset: ASTNode
    ): TextRange? = null

    private fun createSpacingBuilder(settings: CodeStyleSettings): SpacingBuilder = SpacingBuilder(
        settings, ArmaConfigLanguage
    ).around(ArmaConfigTypes.EQUAL).spaces(1).around(ArmaConfigTypes.PLUS).spaces(1).around(ArmaConfigTypes.MINUS)
        .spaces(1).around(ArmaConfigTypes.STAR).spaces(1).around(ArmaConfigTypes.SLASH).spaces(1)
        .around(ArmaConfigTypes.PERCENT).spaces(1).around(ArmaConfigTypes.CARET).spaces(1)
        .around(ArmaConfigTypes.MIN_KEYWORD).spaces(1).around(ArmaConfigTypes.MAX_KEYWORD).spaces(1)
        .around(ArmaConfigTypes.COLON).spaces(1).after(ArmaConfigTypes.COMMA).spaces(1).before(ArmaConfigTypes.COMMA)
        .spaces(0).before(ArmaConfigTypes.SEMICOLON).spaces(0)
}

/**
 * Formatter block:
 *  - skips whitespace tokens
 *  - indents class/array bodies, not headers
 *  - treats SINGLE_QUOTE_BLOCK and MACRO_INVOCATION as opaque
 */
private class ArmaConfigBlock(
    node: ASTNode, wrap: Wrap?, alignment: Alignment?, private val spacingBuilder: SpacingBuilder
) : AbstractBlock(node, wrap, alignment) {

    override fun buildChildren(): List<Block> {
        val type = myNode.elementType

        // Do NOT format inside single-quote blocks or macro invocations
        if (type == ArmaConfigTypes.SINGLE_QUOTE_BLOCK || type == ArmaConfigTypes.MACRO_INVOCATION) return emptyList()

        val blocks = mutableListOf<Block>()
        var child = myNode.firstChildNode

        while (child != null) {
            if (child.textRange.length > 0 && child.elementType != TokenType.WHITE_SPACE) {
                blocks += ArmaConfigBlock(
                    node = child,
                    wrap = Wrap.createWrap(WrapType.NONE, false),
                    alignment = null,
                    spacingBuilder = spacingBuilder
                )
            }
            child = child.treeNext
        }

        return blocks
    }

    override fun isLeaf(): Boolean {
        val type = myNode.elementType
        return when (type) {
            ArmaConfigTypes.SINGLE_QUOTE_BLOCK, ArmaConfigTypes.MACRO_INVOCATION -> true
            else -> myNode.firstChildNode == null
        }
    }

    override fun getIndent(): Indent? {
        val parent = myNode.treeParent ?: return Indent.getNoneIndent()
        val parentType = parent.elementType
        val type = myNode.elementType

        // 1) Top-level: direct children of file → NO INDENT
        if (parent.treeParent == null) {
            return Indent.getNoneIndent()
        }

        // 2) Braces themselves are never indented
        if (type == ArmaConfigTypes.LBRACE ||
            type == ArmaConfigTypes.RBRACE ||
            type == ArmaConfigTypes.LBRACKET ||
            type == ArmaConfigTypes.RBRACKET) {
            return Indent.getNoneIndent()
        }

        // 3) Inside class bodies → indent normally
        if (parentType == ArmaConfigTypes.CLASS_DECL) {
            return Indent.getNormalIndent()
        }

        // 4) Inside arrays or value lists → indent normally
        if (parentType == ArmaConfigTypes.ARRAY ||
            parentType == ArmaConfigTypes.VALUE_LIST) {
            return Indent.getNormalIndent()
        }

        // Default: no indent
        return Indent.getNoneIndent()
    }

    override fun getChildAttributes(newChildIndex: Int): ChildAttributes {
        // When pressing Enter after '{', indent one level
        val indent = if (myNode.elementType == ArmaConfigTypes.LBRACE) {
            Indent.getNormalIndent()
        } else {
            Indent.getNoneIndent()
        }

        return ChildAttributes(indent, null)
    }


    override fun getSpacing(child1: Block?, child2: Block): Spacing? {
        // SPECIAL CASE: keep "+=" glued together
        if (child1 is ArmaConfigBlock && child2 is ArmaConfigBlock) {
            val t1 = child1.node.elementType
            val t2 = child2.node.elementType
            if (t1 == ArmaConfigTypes.PLUS && t2 == ArmaConfigTypes.EQUAL) {
                // no spaces between '+' and '='
                return Spacing.createSpacing(0, 0, 0, false, 0)
            }
        }

        return spacingBuilder.getSpacing(this, child1, child2)
    }
}
