package arma.config.highlighting

import arma.config.ArmaConfigLexer
import arma.config.psi.ArmaConfigTypes
import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
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
        ArmaConfigTypes.STRING -> STRING_KEYS
        ArmaConfigTypes.NUMBER -> NUMBER_KEYS

        // {} braces
        ArmaConfigTypes.LBRACE, ArmaConfigTypes.RBRACE -> BRACES_KEYS

        // [] brackets
        ArmaConfigTypes.LBRACKET, ArmaConfigTypes.RBRACKET -> BRACKETS_KEYS

        // Operators / punctuation
        ArmaConfigTypes.EQUAL, ArmaConfigTypes.SEMICOLON, ArmaConfigTypes.COMMA, ArmaConfigTypes.COLON -> OPERATOR_KEYS

        // Comments
        ArmaConfigTypes.LINE_COMMENT, ArmaConfigTypes.BLOCK_COMMENT -> COMMENT_KEYS

        // Preprocessor stuff (#include, #define, etc)
        ArmaConfigTypes.PREPROCESSOR -> PREPROCESSOR_KEYS

        // Bad characters
        TokenType.BAD_CHARACTER -> BAD_CHAR_KEYS

        else -> EMPTY_KEYS
    }

    companion object {
        // Make these @JvmField so they can be referenced from the ColorSettingsPage

        @JvmField
        val KEYWORD: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
            "ARMA_CONFIG_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD
        )

        @JvmField
        val STRING: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
            "ARMA_CONFIG_STRING", DefaultLanguageHighlighterColors.STRING
        )

        @JvmField
        val NUMBER: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
            "ARMA_CONFIG_NUMBER", DefaultLanguageHighlighterColors.NUMBER
        )

        // {} braces – inherit Language Defaults → Braces
        @JvmField
        val BRACES: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
            "ARMA_CONFIG_BRACES", DefaultLanguageHighlighterColors.BRACES
        )

        // [] brackets – inherit Language Defaults → Brackets
        @JvmField
        val BRACKETS: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
            "ARMA_CONFIG_BRACKETS", DefaultLanguageHighlighterColors.BRACKETS
        )

        // Operators, separators (=, ;, , :)
        @JvmField
        val OPERATOR: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
            "ARMA_CONFIG_OPERATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN
        )

        // Comment style
        @JvmField
        val COMMENT: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
            "ARMA_CONFIG_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT
        )

        // Preprocessor style (metadata-like)
        @JvmField
        val PREPROCESSOR: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
            "ARMA_CONFIG_PREPROCESSOR", DefaultLanguageHighlighterColors.METADATA
        )

        // Bad characters -> use the global “Bad character” style (red by default)
        @JvmField
        val BAD_CHAR: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
            "ARMA_CONFIG_BAD_CHAR", HighlighterColors.BAD_CHARACTER
        )

        // Arrays of keys for convenience
        val KEYWORD_KEYS = arrayOf(KEYWORD)
        val STRING_KEYS = arrayOf(STRING)
        val NUMBER_KEYS = arrayOf(NUMBER)
        val BRACES_KEYS = arrayOf(BRACES)
        val BRACKETS_KEYS = arrayOf(BRACKETS)
        val OPERATOR_KEYS = arrayOf(OPERATOR)
        val COMMENT_KEYS = arrayOf(COMMENT)
        val PREPROCESSOR_KEYS = arrayOf(PREPROCESSOR)
        val BAD_CHAR_KEYS = arrayOf(BAD_CHAR)
        val EMPTY_KEYS = emptyArray<TextAttributesKey>()
    }
}

// Factory that IntelliJ calls when it needs a SyntaxHighlighter for this file type
class ArmaConfigSyntaxHighlighterFactory : SyntaxHighlighterFactory() {
    override fun getSyntaxHighlighter(project: Project?, virtualFile: VirtualFile?): SyntaxHighlighter =
        ArmaConfigSyntaxHighlighter()
}
