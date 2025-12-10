package arma.config

import arma.config.psi.CfgTypes
import com.intellij.lexer.LexerBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType

class CfgLexer : LexerBase() {

    // Entire text of the file being lexed
    private var buffer: CharSequence = ""

    // Start offset (inclusive) of the region being lexed
    private var regionStart: Int = 0

    // End offset (exclusive) of the region being lexed
    private var regionEnd: Int = 0

    // Start offset (inclusive) of the current token
    private var tokenStart: Int = 0

    // End offset (exclusive) of the current token
    private var tokenEnd: Int = 0

    // Type of the current token (or null if EOF)
    private var tokenType: IElementType? = null

    // Called when lexing starts (or restarts for a region of text)
    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        this.buffer = buffer
        this.regionStart = startOffset
        this.regionEnd = endOffset
        // advance() uses tokenEnd as the next tokenStart (as if where last token ended)
        this.tokenEnd = startOffset
        // Immediately advance to the first token
        advance()
    }

    // We have no multi-state lexing, so always return a single state
    override fun getState(): Int = 0

    // Current token type
    override fun getTokenType(): IElementType? = tokenType

    // Current token start offset
    override fun getTokenStart(): Int = tokenStart

    // Current token end offset (exclusive)
    override fun getTokenEnd(): Int = tokenEnd

    // Underlying character buffer
    override fun getBufferSequence(): CharSequence = buffer

    // End offset of the buffer
    override fun getBufferEnd(): Int = regionEnd

    // Move to the next token
    override fun advance() {
        tokenStart = tokenEnd
        if (tokenStart < regionStart) tokenStart = regionStart
        if (tokenStart >= regionEnd) {
            tokenType = null
            tokenStart = regionEnd
            tokenEnd = regionEnd
            return
        }
        tokenEnd = tokenStart + 1
        val tokenChar = buffer[tokenStart]

        // WHITESPACE token
        if (tokenChar.isWhitespace()) {
            while (tokenEnd < regionEnd && buffer[tokenEnd].isWhitespace()) tokenEnd++
            tokenType = TokenType.WHITE_SPACE
            return
        }

        // If not first in line than ## DOUBLE_HASH or # SINGLE_HASH
        // If solo # than SINGLE_HASH
        // If first in line with existing directive keyword than PREPROCESSOR
        // + multiline checking if #define
        if (tokenChar == '#') {
            if (tokenEnd >= regionEnd) {
                tokenType = CfgTypes.SINGLE_HASH
                return
            }
            //check if first non-whitespace in line
            var back = tokenStart - 1
            var isFirstNonWsOnLine = true
            while (back >= regionStart) {
                val char = buffer[back]
                if (char == '\n' || char == '\r') break
                if (!char.isWhitespace()) {
                    isFirstNonWsOnLine = false
                    break
                }
                back--
            }

            //if not first in line, then check if ## or just #
            if (!isFirstNonWsOnLine) {
                if (buffer[tokenEnd] == '#') {
                    tokenEnd++
                    tokenType = CfgTypes.DOUBLE_HASH
                    return
                }
                tokenType = CfgTypes.SINGLE_HASH
                return
            }

            //check if a keyword starts after #
            if (buffer[tokenEnd] !in 'a'..'z') {
                tokenType = CfgTypes.SINGLE_HASH
                return
            }
            //grab full keyword
            while (tokenEnd < regionEnd && buffer[tokenEnd] in 'a'..'z') tokenEnd++
            //check if string (without `#`) is a valid preprocessor directive
            val isDefine = when (buffer.subSequence(tokenStart + 1, tokenEnd).toString()) {
                "define" -> true
                "include",
                "undef",
                "if",
                "ifdef",
                "ifndef",
                "else",
                "endif" -> false

                else -> {
                    tokenType = TokenType.BAD_CHARACTER
                    return
                }
            }
            //grab until end of line
            //if define check last char, and continue to newline if \
            while (tokenEnd < regionEnd) {
                val char = buffer[tokenEnd]
                if (char == '\n' || char == '\r') {
                    if (!isDefine || buffer[tokenEnd - 1] != '\\') break
                    // For #define, allow line continuation if last character before newline is '\'
                    tokenEnd++
                    if (char == '\r' && tokenEnd < regionEnd && buffer[tokenEnd] == '\n') tokenEnd++
                    // End of directive (for non-define, or define without continuation)
                } else tokenEnd++
            }
            tokenType = CfgTypes.PREPROCESSOR
            return
        }

        // COMMENTS: either // line comment or /* block comment */
        if (tokenChar == '/' && tokenEnd < regionEnd) {
            // line comment: // ...
            if (buffer[tokenEnd] == '/') {
                tokenEnd++
                while (tokenEnd < regionEnd) {
                    val ch = buffer[tokenEnd]
                    if (ch == '\n' || ch == '\r') break // until newline
                    tokenEnd++
                }
                tokenType = CfgTypes.LINE_COMMENT
                return
            }

            // block comment: /* ... */
            if (buffer[tokenEnd] == '*') {
                tokenEnd++
                while (tokenEnd < regionEnd - 1) {
                    if (buffer[tokenEnd] == '*' && buffer[tokenEnd + 1] == '/') {
                        tokenEnd += 2
                        tokenType = CfgTypes.BLOCK_COMMENT
                        return
                    }
                    tokenEnd++
                }
                tokenEnd++
                tokenType = TokenType.BAD_CHARACTER
                return
            }
        }

        // IDENT or keyword (also used for path-like tokens inside macros):
        // starts with letter / '_' / path chars, continues with letters/digits/underscore/path chars
        if (tokenChar.isValidText()) {
            while (tokenEnd < regionEnd && buffer[tokenEnd].isValidTextOrDigit()) tokenEnd++
            tokenType = when (buffer.subSequence(this.tokenStart, this.tokenEnd).toString()) {
                "class" -> CfgTypes.CLASS_KEYWORD
                "delete" -> CfgTypes.DELETE_KEYWORD
                "min" -> CfgTypes.MIN_KEYWORD
                "max" -> CfgTypes.MAX_KEYWORD
                else -> CfgTypes.TEXT
            }
            return
        }

        // NUMBER, FLOAT, or identifier starting with a digit
        // e.g. "30Rnd_556x45_Stanag" should be a single IDENT, not NUMBER("30") + IDENT("Rnd_...")
        if (tokenChar.isValidDigit()) {
            var seenDot = false
            var seenExponent = false
            var seenOperator = false
            var seenText = false
            while (tokenEnd < regionEnd) {
                val char = buffer[tokenEnd]
                when {
                    char.isValidDigit() -> tokenEnd++

                    // first dot followed by a digit -> part of a FLOAT literal
                    char == '.' && !seenDot && !seenExponent -> {
                        seenDot = true
                        tokenEnd++
                        if (tokenEnd >= regionEnd) {
                            tokenType = TokenType.BAD_CHARACTER
                            return
                        }
                        if (buffer[tokenEnd].isValidDigit()) tokenEnd++
                        else {
                            tokenType = TokenType.BAD_CHARACTER
                            return
                        }

                    }

                    // exponent part: e[+/-]?digits  (e.g. 9.999e-006)
                    (char == 'e' || char == 'E') && !seenExponent && !seenText -> {
                        seenExponent = true
                        tokenEnd++
                        if (tokenEnd >= regionEnd) {
                            tokenType = TokenType.BAD_CHARACTER
                            return
                        }
                        val next = buffer[tokenEnd]
                        if (next == '+' || next == '-') {
                            seenOperator = true
                            tokenEnd++
                        }
                        if (tokenEnd >= regionEnd) {
                            tokenType = TokenType.BAD_CHARACTER
                            return
                        }
                        if (buffer[tokenEnd].isValidDigit()) tokenEnd++
                        else {
                            tokenType = TokenType.BAD_CHARACTER
                            return
                        }
                    }

                    // Any char that is valid *inside* an identifier but not a pure number
                    // means we should treat the whole run as IDENT, not NUMBER/FLOAT.
                    !seenDot && !seenOperator && char.isValidText() -> {
                        seenText = true
                        tokenEnd++
                    }

                    else -> break
                }
            }

            tokenType = if (seenText) when (buffer.subSequence(this.tokenStart, this.tokenEnd).toString()) {
                "class" -> CfgTypes.CLASS_KEYWORD
                "delete" -> CfgTypes.DELETE_KEYWORD
                "min" -> CfgTypes.MIN_KEYWORD
                "max" -> CfgTypes.MAX_KEYWORD
                else -> CfgTypes.TEXT
            } else CfgTypes.NUMBER
            return
        }

        // STRING:
        //  - only " ... "
        //  - NO multiline (newline ends the token, leaving it unterminated)
        //  - "" inside acts as an escaped quote and does NOT terminate the string
        //  - backslash has NO special meaning here
        if (tokenChar == '"') {
            while (tokenEnd < regionEnd) {
                when (buffer[tokenEnd]) {
                    '\n', '\r' -> break
                    '"' -> {
                        tokenEnd++
                        if (tokenEnd >= regionEnd) break
                        if (buffer[tokenEnd] == '"') tokenEnd++
                        else break
                    }
                    else -> tokenEnd++
                }
            }
            tokenType = CfgTypes.STRING
            return
        }

        // SINGLE_QUOTE_BLOCK_TOKEN:
        //  - starts with '
        //  - ends at the last ' before a comment or newline
        //  - everything between is opaque (can contain ;, (), macros, "" strings, etc.)
        //  - no multiline: we stop scanning at newline; if we never find a closing ', treat as bad
        // fixme this will work weird with comments, may have unexpected behaviour, maybe it should ignore comments for now
        // fixme if there is a valid string before comment, but comment has ' than idk
        // fixme if there is only one ' before comment and second is inside comment than it will end up ignored, but maybe it should not? Espescially block comments maybe could be inside of this legally
        if (tokenChar == '\'') {
            while (tokenEnd < regionEnd) {
                if (buffer[tokenEnd] == '\n' || buffer[tokenEnd] == '\r') break
                if (buffer[tokenEnd - 1] == '/' && buffer[tokenEnd] == '/') break
                if (buffer[tokenEnd - 1] == '/' && buffer[tokenEnd] == '*') break
                tokenEnd++
            }
            tokenEnd--
            while (tokenEnd > tokenStart) {
                if (buffer[tokenEnd] == '\'') {
                    tokenEnd++
                    tokenType = CfgTypes.SINGLE_QUOTE
                    return
                }
                tokenEnd--
            }
            tokenEnd++
            tokenType = TokenType.BAD_CHARACTER
            return
        }

        // Single-character punctuation tokens
        tokenType = when (tokenChar) {
            '{' -> CfgTypes.LBRACE
            '}' -> CfgTypes.RBRACE
            '=' -> CfgTypes.EQUAL
            ';' -> CfgTypes.SEMICOLON
            '[' -> CfgTypes.LBRACKET
            ']' -> CfgTypes.RBRACKET
            ',' -> CfgTypes.COMMA
            ':' -> CfgTypes.COLON
            '(' -> CfgTypes.LPAREN
            ')' -> CfgTypes.RPAREN
            '+' -> CfgTypes.PLUS
            '-' -> CfgTypes.MINUS
            '*' -> CfgTypes.STAR
            '/' -> CfgTypes.SLASH
            '%' -> CfgTypes.PERCENT
            '^' -> CfgTypes.CARET
            '<' -> CfgTypes.LT
            '>' -> CfgTypes.GT
            '!' -> CfgTypes.EXCL
            '&' -> CfgTypes.AMPERSAND
            '\\' -> CfgTypes.BACKSLASH
            '.' -> CfgTypes.DOT
            else -> TokenType.BAD_CHARACTER
        }
    }

    private fun Char.isValidText(): Boolean = this in 'a'..'z' || this in 'A'..'Z' || this == '_'
    private fun Char.isValidDigit(): Boolean = this in '0'..'9'
    private fun Char.isValidTextOrDigit(): Boolean = this.isValidText() || this.isValidDigit()

}
