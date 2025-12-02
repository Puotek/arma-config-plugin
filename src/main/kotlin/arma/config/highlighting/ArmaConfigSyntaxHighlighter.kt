package arma.config.highlighting

import arma.config.ArmaConfigLexer
import arma.config.psi.ArmaConfigTypes
import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType

class ArmaConfigSyntaxHighlighter : SyntaxHighlighter {

    override fun getHighlightingLexer(): Lexer = ArmaConfigLexer()

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> = when (tokenType) {
        ArmaConfigTypes.CLASS_KEYWORD -> KEYWORD_KEYS
        ArmaConfigTypes.STRING        -> STRING_KEYS
        ArmaConfigTypes.NUMBER        -> NUMBER_KEYS

        ArmaConfigTypes.LBRACE,
        ArmaConfigTypes.RBRACE,
        ArmaConfigTypes.LBRACKET,
        ArmaConfigTypes.RBRACKET      -> BRACES_KEYS

        ArmaConfigTypes.EQUAL,
        ArmaConfigTypes.SEMICOLON,
        ArmaConfigTypes.COMMA,
        ArmaConfigTypes.COLON         -> OPERATOR_KEYS

        ArmaConfigTypes.LINE_COMMENT,
        ArmaConfigTypes.BLOCK_COMMENT -> COMMENT_KEYS

        ArmaConfigTypes.PREPROCESSOR  -> PREPROCESSOR_KEYS

        TokenType.BAD_CHARACTER       -> BAD_CHAR_KEYS
        else                          -> EMPTY_KEYS
    }


    companion object {
        private val KEYWORD = TextAttributesKey.createTextAttributesKey(
            "ARMA_CONFIG_KEYWORD",
            DefaultLanguageHighlighterColors.KEYWORD
        )
        private val STRING = TextAttributesKey.createTextAttributesKey(
            "ARMA_CONFIG_STRING",
            DefaultLanguageHighlighterColors.STRING
        )
        private val NUMBER = TextAttributesKey.createTextAttributesKey(
            "ARMA_CONFIG_NUMBER",
            DefaultLanguageHighlighterColors.NUMBER
        )
        private val BRACES = TextAttributesKey.createTextAttributesKey(
            "ARMA_CONFIG_BRACES",
            DefaultLanguageHighlighterColors.BRACES
        )
        private val OPERATOR = TextAttributesKey.createTextAttributesKey(
            "ARMA_CONFIG_OPERATOR",
            DefaultLanguageHighlighterColors.OPERATION_SIGN
        )
        private val BAD_CHAR = TextAttributesKey.createTextAttributesKey(
            "ARMA_CONFIG_BAD_CHAR",
            DefaultLanguageHighlighterColors.INVALID_STRING_ESCAPE
        )

        private val KEYWORD_KEYS  = arrayOf(KEYWORD)
        private val STRING_KEYS   = arrayOf(STRING)
        private val NUMBER_KEYS   = arrayOf(NUMBER)
        private val BRACES_KEYS   = arrayOf(BRACES)
        private val OPERATOR_KEYS = arrayOf(OPERATOR)
        private val BAD_CHAR_KEYS = arrayOf(BAD_CHAR)
        private val EMPTY_KEYS    = emptyArray<TextAttributesKey>()
        private val COMMENT = TextAttributesKey.createTextAttributesKey(
            "ARMA_CONFIG_COMMENT",
            DefaultLanguageHighlighterColors.LINE_COMMENT
        )

        private val PREPROCESSOR = TextAttributesKey.createTextAttributesKey(
            "ARMA_CONFIG_PREPROCESSOR",
            DefaultLanguageHighlighterColors.METADATA // or KEYWORD, up to you
        )

        private val COMMENT_KEYS     = arrayOf(COMMENT)
        private val PREPROCESSOR_KEYS = arrayOf(PREPROCESSOR)

    }
}

class ArmaConfigSyntaxHighlighterFactory : SyntaxHighlighterFactory() {
    override fun getSyntaxHighlighter(project: Project?, virtualFile: VirtualFile?): SyntaxHighlighter =
        ArmaConfigSyntaxHighlighter()
}
