package arma.config.formatting

import arma.config.ArmaConfigLanguage
import arma.config.psi.ArmaConfigTypes
import com.intellij.formatting.Alignment
import com.intellij.formatting.Block
import com.intellij.formatting.ChildAttributes
import com.intellij.formatting.FormattingContext
import com.intellij.formatting.FormattingModel
import com.intellij.formatting.FormattingModelBuilder
import com.intellij.formatting.FormattingModelProvider
import com.intellij.formatting.Indent
import com.intellij.formatting.Spacing
import com.intellij.formatting.SpacingBuilder
import com.intellij.formatting.Wrap
import com.intellij.formatting.WrapType
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
    )
        // assignment
        .around(ArmaConfigTypes.EQUAL).spaces(1)

        // arithmetic and min/max
        .around(ArmaConfigTypes.PLUS).spaces(1).around(ArmaConfigTypes.MINUS).spaces(1).around(ArmaConfigTypes.STAR)
        .spaces(1).around(ArmaConfigTypes.SLASH).spaces(1).around(ArmaConfigTypes.PERCENT).spaces(1)
        .around(ArmaConfigTypes.CARET).spaces(1).around(ArmaConfigTypes.MIN_KEYWORD).spaces(1)
        .around(ArmaConfigTypes.MAX_KEYWORD).spaces(1)

        // spaces around ':' (used for inheritance)
        .around(ArmaConfigTypes.COLON).spaces(1)

        // commas: a, b
        .after(ArmaConfigTypes.COMMA).spaces(1)

        // no space before ;
        .before(ArmaConfigTypes.SEMICOLON).spaces(0)
}

/**
 * Basic formatter block:
 *  - skips whitespace tokens
 *  - indents body of classes and arrays
 *  - keeps class headers (class keyword/name/colon/braces) at the class indent
 *  - treats SINGLE_QUOTE_BLOCK and MACRO_INVOCATION as opaque (no formatting inside)
 */
private class ArmaConfigBlock(
    node: ASTNode, wrap: Wrap?, alignment: Alignment?, private val spacingBuilder: SpacingBuilder
) : AbstractBlock(node, wrap, alignment) {

    override fun buildChildren(): List<Block> {
        val type = myNode.elementType

        // Do NOT format inside single-quote blocks or macro invocations: treat as opaque
        if (type == ArmaConfigTypes.SINGLE_QUOTE_BLOCK || type == ArmaConfigTypes.MACRO_INVOCATION) {
            return emptyList()
        }

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

    override fun getIndent(): Indent? {
        val parentType = myNode.treeParent?.elementType
        val type = myNode.elementType

        // Root: no indent
        if (parentType == null) {
            return Indent.getNoneIndent()
        }

        // Braces / brackets themselves stay flush
        if (type == ArmaConfigTypes.LBRACE || type == ArmaConfigTypes.RBRACE || type == ArmaConfigTypes.LBRACKET || type == ArmaConfigTypes.RBRACKET) {
            return Indent.getNoneIndent()
        }

        // Inside a class declaration:
        //  - class header tokens stay at class indent (NONE)
        //  - body elements (nested classes, assignments, comments, etc.) get NORMAL indent
        if (parentType == ArmaConfigTypes.CLASS_DECL) {
            // comments inside class behave like body items
            if (type == ArmaConfigTypes.LINE_COMMENT || type == ArmaConfigTypes.BLOCK_COMMENT) {
                return Indent.getNormalIndent()
            }

            return when (type) {
                // body-level constructs inside a class
                ArmaConfigTypes.CLASS_DECL, ArmaConfigTypes.CLASS_FORWARD_DECL, ArmaConfigTypes.ASSIGNMENT, ArmaConfigTypes.DELETE_STMT, ArmaConfigTypes.MACRO_STMT -> Indent.getNormalIndent()

                // everything else under CLASS_DECL (class keyword, name, colon, braces, semicolon)
                else -> Indent.getNoneIndent()
            }
        }

        // Inside an array / value list:
        //  - values + comments get NORMAL indent
        return when (parentType) {
            ArmaConfigTypes.ARRAY, ArmaConfigTypes.VALUE_LIST -> when (type) {
                ArmaConfigTypes.VALUE_LIST, ArmaConfigTypes.LINE_COMMENT, ArmaConfigTypes.BLOCK_COMMENT -> Indent.getNormalIndent()
                else -> Indent.getNoneIndent()
            }

            else -> Indent.getNoneIndent()
        }
    }

    override fun getChildIndent(): Indent? {
        val type = myNode.elementType

        // When pressing Enter inside a class or array, indent new children
        return when (type) {
            ArmaConfigTypes.CLASS_DECL, ArmaConfigTypes.ARRAY -> Indent.getNormalIndent()

            else -> Indent.getNoneIndent()
        }
    }

    override fun getChildAttributes(newChildIndex: Int): ChildAttributes = ChildAttributes(getChildIndent(), null)

    override fun isLeaf(): Boolean {
        val type = myNode.elementType

        // Treat single-quote blocks and macro invocations as leaves:
        // formatter never recurses into them
        return when (type) {
            ArmaConfigTypes.SINGLE_QUOTE_BLOCK, ArmaConfigTypes.MACRO_INVOCATION -> true

            else -> myNode.firstChildNode == null
        }
    }

    override fun getSpacing(child1: Block?, child2: Block): Spacing? = spacingBuilder.getSpacing(this, child1, child2)
}
