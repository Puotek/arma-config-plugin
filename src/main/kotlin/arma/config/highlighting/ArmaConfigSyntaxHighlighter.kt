package arma.config.highlighting

import arma.config.ArmaConfigLexer          // Our lexer
import arma.config.psi.ArmaConfigTypes      // Token/element types
import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType

// Actual highlighter implementation: maps tokens -> colors
class ArmaConfigSyntaxHighlighter : SyntaxHighlighter {

    // Lexer used for highlighting
    override fun getHighlightingLexer(): Lexer = ArmaConfigLexer()

    // Maps a token type to one or more TextAttributesKey (color styles)
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
        // KEYWORD style, based on default language keyword color
        private val KEYWORD = TextAttributesKey.createTextAttributesKey(
            "ARMA_CONFIG_KEYWORD",
            DefaultLanguageHighlighterColors.KEYWORD
        )
        // STRING style
        private val STRING = TextAttributesKey.createTextAttributesKey(
            "ARMA_CONFIG_STRING",
            DefaultLanguageHighlighterColors.STRING
        )
        // NUMBER style
        private val NUMBER = TextAttributesKey.createTextAttributesKey(
            "ARMA_CONFIG_NUMBER",
            DefaultLanguageHighlighterColors.NUMBER
        )
        // BRACES style for {}, []
        private val BRACES = TextAttributesKey.createTextAttributesKey(
            "ARMA_CONFIG_BRACES",
            DefaultLanguageHighlighterColors.BRACES
        )
        // Operators, separators (=, ;, , :)
        private val OPERATOR = TextAttributesKey.createTextAttributesKey(
            "ARMA_CONFIG_OPERATOR",
            DefaultLanguageHighlighterColors.OPERATION_SIGN
        )
        // Bad characters / invalid tokens
        private val BAD_CHAR = TextAttributesKey.createTextAttributesKey(
            "ARMA_CONFIG_BAD_CHAR",
            DefaultLanguageHighlighterColors.INVALID_STRING_ESCAPE
        )

        // Arrays of keys for convenience
        private val KEYWORD_KEYS  = arrayOf(KEYWORD)
        private val STRING_KEYS   = arrayOf(STRING)
        private val NUMBER_KEYS   = arrayOf(NUMBER)
        private val BRACES_KEYS   = arrayOf(BRACES)
        private val OPERATOR_KEYS = arrayOf(OPERATOR)
        private val BAD_CHAR_KEYS = arrayOf(BAD_CHAR)
        private val EMPTY_KEYS    = emptyArray<TextAttributesKey>()

        // Comment style
        private val COMMENT = TextAttributesKey.createTextAttributesKey(
            "ARMA_CONFIG_COMMENT",
            DefaultLanguageHighlighterColors.LINE_COMMENT
        )

        // Preprocessor style (using METADATA for now)
        private val PREPROCESSOR = TextAttributesKey.createTextAttributesKey(
            "ARMA_CONFIG_PREPROCESSOR",
            DefaultLanguageHighlighterColors.METADATA // you can choose something else
        )

        // Arrays for comments and preprocessor tokens
        private val COMMENT_KEYS      = arrayOf(COMMENT)
        private val PREPROCESSOR_KEYS = arrayOf(PREPROCESSOR)

    }
}

// Factory that IntelliJ calls when it needs a SyntaxHighlighter for this file type
class ArmaConfigSyntaxHighlighterFactory : SyntaxHighlighterFactory() {
    override fun getSyntaxHighlighter(project: Project?, virtualFile: VirtualFile?): SyntaxHighlighter =
        ArmaConfigSyntaxHighlighter()
}
