package arma.config.highlighting

import arma.config.CfgLexer
import arma.config.psi.CfgTypes
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
class CfgSyntaxHighlighter : SyntaxHighlighter {

    // Lexer used for highlighting
    override fun getHighlightingLexer(): Lexer = CfgLexer()

    // Maps a token type to one or more TextAttributesKey (color styles)
    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> = when (tokenType) {
        CfgTypes.CLASS_KEYWORD,
        CfgTypes.DELETE_KEYWORD -> KEYWORD_KEYS

        CfgTypes.STRING -> STRING_KEYS

        CfgTypes.SINGLE_QUOTE -> SINGLE_QUOTE_BLOCK_KEYS

        CfgTypes.NUMBER -> NUMBER_KEYS

        CfgTypes.LBRACE,
        CfgTypes.RBRACE -> BRACES_KEYS

        CfgTypes.LBRACKET,
        CfgTypes.RBRACKET -> BRACKETS_KEYS

        CfgTypes.LPAREN,
        CfgTypes.RPAREN -> PAREN_KEYS

        CfgTypes.PLUS,
        CfgTypes.MINUS,
        CfgTypes.STAR,
        CfgTypes.SLASH,
        CfgTypes.PERCENT,
        CfgTypes.CARET,
        CfgTypes.MIN_KEYWORD,
        CfgTypes.MAX_KEYWORD,
        CfgTypes.EQUAL,
        CfgTypes.SEMICOLON,
        CfgTypes.COMMA,
        CfgTypes.AMPERSAND,
        CfgTypes.COLON -> OPERATOR_KEYS

        CfgTypes.LINE_COMMENT,
        CfgTypes.BLOCK_COMMENT -> COMMENT_KEYS

        CfgTypes.PREPROCESSOR -> PREPROCESSOR_KEYS

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

        // () parentheses – inherit Language Defaults → Parentheses
        @JvmField
        val PAREN: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
            "ARMA_CONFIG_PAREN", DefaultLanguageHighlighterColors.PARENTHESES
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

        @JvmField
        val SINGLE_QUOTE_BLOCK: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
            "ARMA_CONFIG_SINGLE_QUOTE_BLOCK",
            // Base it on STRING so theme inheritance is nice, but user can change it separately
            DefaultLanguageHighlighterColors.VALID_STRING_ESCAPE
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
        val PAREN_KEYS = arrayOf(PAREN)
        val OPERATOR_KEYS = arrayOf(OPERATOR)
        val COMMENT_KEYS = arrayOf(COMMENT)
        val PREPROCESSOR_KEYS = arrayOf(PREPROCESSOR)
        val BAD_CHAR_KEYS = arrayOf(BAD_CHAR)
        val EMPTY_KEYS = emptyArray<TextAttributesKey>()
        val SINGLE_QUOTE_BLOCK_KEYS = arrayOf(SINGLE_QUOTE_BLOCK)
    }
}

// Factory that IntelliJ calls when it needs a SyntaxHighlighter for this file type
class CfgSyntaxHighlighterFactory : SyntaxHighlighterFactory() {
    override fun getSyntaxHighlighter(
        project: Project?, virtualFile: VirtualFile?
    ): SyntaxHighlighter = CfgSyntaxHighlighter()
}
