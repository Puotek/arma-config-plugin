package arma.config

import arma.config.psi.ArmaConfigTypes
import com.intellij.lexer.LexerBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType

class ArmaConfigLexer : LexerBase() {

    // Entire text of the file being lexed
    private var buffer: CharSequence = ""

    // Start offset of the region being lexed
    private var startOffset: Int = 0

    // End offset of the region being lexed
    private var endOffset: Int = 0

    // Start offset of the current token
    private var tokenStart: Int = 0

    // End offset (exclusive) of the current token
    private var tokenEnd: Int = 0

    // Type of the current token (or null if EOF)
    private var tokenType: IElementType? = null

    // Called when lexing starts (or restarts for a region of text)
    override fun start(
        buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int
    ) {
        // Store the input buffer and region
        this.buffer = buffer
        this.startOffset = startOffset
        this.endOffset = endOffset

        // Initialize current token range and type
        this.tokenStart = startOffset
        this.tokenEnd = startOffset
        this.tokenType = null

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
    override fun getBufferEnd(): Int = endOffset

    // Move to the next token
    override fun advance() {
        if (tokenEnd < startOffset) tokenEnd = startOffset
        if (tokenEnd >= endOffset) {
            tokenType = null
            tokenStart = endOffset
            tokenEnd = endOffset
            return
        }
        tokenStart = tokenEnd
        val tokenChar = buffer[tokenStart]

        // WHITESPACE token
        if (tokenChar.isWhitespace()) {
            tokenEnd = tokenStart + 1
            while (tokenEnd < endOffset && buffer[tokenEnd].isWhitespace()) tokenEnd++
            tokenType = TokenType.WHITE_SPACE
            return
        }

        // PREPROCESSOR: '#' as the first non-whitespace on a line
        // - May have spaces/tabs before '#'
        // - Must be the first non-space char since the previous newline
        // - '\' continuation ONLY for #define
        if (tokenChar == '#') {
            //check if first non-whitespace in line
            var back = tokenStart - 1
            var isFirstNonWsOnLine = true
            while (back >= startOffset) {
                val char = buffer[back]
                if (char == '\n' || char == '\r') break
                if (!char.isWhitespace()) {
                    isFirstNonWsOnLine = false
                    break
                }
                back--
            }

            tokenEnd = tokenStart + 1
            //if not first in line, then check if ## or just #
            if (!isFirstNonWsOnLine) {
                if (tokenEnd >= endOffset || buffer[tokenEnd] != '#') {
                    tokenType = ArmaConfigTypes.SINGLE_HASH
                    return
                } else {
                    tokenEnd++
                    tokenType = ArmaConfigTypes.DOUBLE_HASH
                    return
                }
            }

            //check if a keyword starts after #
            if (tokenEnd >= endOffset || buffer[tokenEnd] !in 'a'..'z') {
                tokenType = ArmaConfigTypes.SINGLE_HASH
                return
            }
            //grab full keyword
            while (tokenEnd < endOffset && buffer[tokenEnd] in 'a'..'z') tokenEnd++
            //check if string is a valid preprocessor directive
            val isDefine = when (buffer.subSequence(tokenStart + 1, tokenEnd).toString()) {
                "define" -> true
                "include", "undef", "if", "ifdef", "ifndef", "else", "endif"
                    -> false

                else -> {
                    tokenType = TokenType.BAD_CHARACTER
                    return
                }
            }
            //grab until end of line
            //if define check last char, and continue to newline if \
            while (tokenEnd < endOffset) {
                val char = buffer[tokenEnd]
                if (char == '\n' || char == '\r') {
                    if (!isDefine || buffer[tokenEnd - 1] != '\\') break
                    // For #define, allow line continuation if last character before newline is '\'
                    tokenEnd++
                    if (char == '\r' && tokenEnd < endOffset && buffer[tokenEnd] == '\n') tokenEnd++
                    continue
                    // End of directive (for non-define, or define without continuation)
                }
                tokenEnd++
            }
            tokenType = ArmaConfigTypes.PREPROCESSOR
            return
        }

        //fixme I want to rewrite all of the below at some point myself
        // COMMENTS: either // line comment or /* block comment */
        if (tokenChar == '/' && tokenStart + 1 < endOffset) {
            val next = buffer[tokenStart + 1]

            // line comment: // ...
            if (next == '/') {
                var j = tokenStart + 2
                while (j < endOffset) {
                    val ch = buffer[j]
                    if (ch == '\n' || ch == '\r') break // until newline
                    j++
                }
                tokenEnd = j
                tokenType = ArmaConfigTypes.LINE_COMMENT
                return
            }

            // block comment: /* ... */
            if (next == '*') {
                var j = tokenStart + 2
                while (j < endOffset - 1) {
                    if (buffer[j] == '*' && buffer[j + 1] == '/') {
                        j += 2   // include closing "*/"
                        break
                    }
                    j++
                }
                if (j > endOffset) j = endOffset
                tokenEnd = j
                tokenType = ArmaConfigTypes.BLOCK_COMMENT
                return
            }
        }

        // IDENT or keyword (also used for path-like tokens inside macros):
        // starts with letter / '_' / path chars, continues with letters/digits/underscore/path chars
        if (tokenChar.isIdentStart()) {
            var j = tokenStart + 1
            while (j < endOffset && buffer[j].isIdentPart()) {
                j++
            }
            tokenEnd = j
            val text = buffer.subSequence(tokenStart, tokenEnd).toString()
            // Recognize keywords; otherwise IDENT
            tokenType = when (text) {
                "class" -> ArmaConfigTypes.CLASS_KEYWORD
                "delete" -> ArmaConfigTypes.DELETE_KEYWORD
                "min" -> ArmaConfigTypes.MIN_KEYWORD
                "max" -> ArmaConfigTypes.MAX_KEYWORD
                else -> ArmaConfigTypes.TEXT
            }
            return
        }

        // NUMBER, FLOAT, or identifier starting with a digit
        // e.g. "30Rnd_556x45_Stanag" should be a single IDENT, not NUMBER("30") + IDENT("Rnd_...")
        if (tokenChar.isDigit()) {
            var j = tokenStart
            var seenDot = false
            var seenExponent = false
            var isIdentLike = false

            while (j < endOffset) {
                val ch = buffer[j]
                when {
                    ch.isDigit() -> {
                        j++
                    }

                    // first dot followed by a digit -> part of a FLOAT literal
                    !seenDot && ch == '.' && j + 1 < endOffset && buffer[j + 1].isDigit() -> {
                        seenDot = true
                        j++ // consume '.'
                    }

                    // exponent part: e[+/-]?digits  (e.g. 9.999e-006)
                    !seenExponent && (ch == 'e' || ch == 'E') && j > tokenStart -> {
                        var k = j + 1
                        // optional sign
                        if (k < endOffset && (buffer[k] == '+' || buffer[k] == '-')) {
                            k++
                        }
                        val digitsStart = k
                        while (k < endOffset && buffer[k].isDigit()) {
                            k++
                        }

                        if (k == digitsStart) {
                            // no digits after e / e+ / e- → treat 'e' as ident char
                            isIdentLike = true
                            j++
                        } else {
                            seenExponent = true
                            j = k
                        }
                    }

                    // Any char that is valid *inside* an identifier but not a pure number
                    // means we should treat the whole run as IDENT, not NUMBER/FLOAT.
                    ch.isIdentPart() -> {
                        isIdentLike = true
                        j++
                    }

                    else -> break
                }
            }

            tokenEnd = j

            if (isIdentLike) {
                val text = buffer.subSequence(tokenStart, tokenEnd).toString()
                tokenType = when (text) {
                    "class" -> ArmaConfigTypes.CLASS_KEYWORD
                    "delete" -> ArmaConfigTypes.DELETE_KEYWORD
                    "min" -> ArmaConfigTypes.MIN_KEYWORD
                    "max" -> ArmaConfigTypes.MAX_KEYWORD
                    else -> ArmaConfigTypes.TEXT
                }
            } else {
                // exponent or dot => FLOAT, otherwise NUMBER
                tokenType = if (seenDot || seenExponent) ArmaConfigTypes.FLOAT
                else ArmaConfigTypes.NUMBER
            }
            return
        }

        // STRING:
        //  - only " ... "
        //  - NO multiline (newline ends the token, leaving it unterminated)
        //  - "" inside acts as an escaped quote and does NOT terminate the string
        //  - backslash has NO special meaning here
        if (tokenChar == '"') {
            var j = tokenStart + 1

            while (j < endOffset) {
                val ch = buffer[j]

                if (ch == '\n' || ch == '\r') {
                    break
                }

                if (ch == '"') {
                    // Handle doubled quote "" inside the string
                    if (j + 1 < endOffset && buffer[j + 1] == '"') {
                        j += 2
                        continue
                    }

                    // Single " -> close string
                    j++
                    break
                }

                j++
            }

            if (j > endOffset) j = endOffset
            tokenEnd = j
            tokenType = ArmaConfigTypes.STRING
            return
        }

        // SINGLE_QUOTE_BLOCK_TOKEN:
        //  - starts with '
        //  - ends at the last ' before a comment or newline
        //  - everything between is opaque (can contain ;, (), macros, "" strings, etc.)
        //  - no multiline: we stop scanning at newline; if we never find a closing ', treat as bad
        if (tokenChar == '\'') {
            var j = tokenStart + 1
            var lastQuote = -1

            while (j < endOffset) {
                val ch = buffer[j]

                // Stop at newline: single-quote blocks cannot be multiline
                if (ch == '\n' || ch == '\r') {
                    break
                }

                // Stop scanning before comments: // or /* start
                if (ch == '/' && j + 1 < endOffset) {
                    val next = buffer[j + 1]
                    if (next == '/' || next == '*') {
                        break
                    }
                }

                if (ch == '\'') {
                    lastQuote = j
                }

                j++
            }

            if (lastQuote == -1) {
                // No closing ' found on this line before comment/newline -> stray quote
                tokenEnd = tokenStart + 1
                tokenType = TokenType.BAD_CHARACTER
                return
            }

            tokenEnd = lastQuote + 1 // include the closing '
            tokenType = ArmaConfigTypes.SINGLE_QUOTE
            return
        }

        // Single-character punctuation tokens
        tokenEnd = tokenStart + 1
        tokenType = when (tokenChar) {
            '{' -> ArmaConfigTypes.LBRACE
            '}' -> ArmaConfigTypes.RBRACE
            '=' -> ArmaConfigTypes.EQUAL
            ';' -> ArmaConfigTypes.SEMICOLON
            '[' -> ArmaConfigTypes.LBRACKET
            ']' -> ArmaConfigTypes.RBRACKET
            ',' -> ArmaConfigTypes.COMMA
            ':' -> ArmaConfigTypes.COLON
            '(' -> ArmaConfigTypes.LPAREN
            ')' -> ArmaConfigTypes.RPAREN
            '+' -> ArmaConfigTypes.PLUS
            '-' -> ArmaConfigTypes.MINUS
            '*' -> ArmaConfigTypes.STAR
            '/' -> ArmaConfigTypes.SLASH
            '%' -> ArmaConfigTypes.PERCENT
            '^' -> ArmaConfigTypes.CARET
            '<' -> ArmaConfigTypes.LT
            '>' -> ArmaConfigTypes.GT
            '!' -> ArmaConfigTypes.EXCL
            else -> TokenType.BAD_CHARACTER  // Unknown/invalid char
        }
    }

    // Helper: we don’t use the built-in isLetterOrDigit for this Char type,
    // so we define it via isLetter()+isDigit()
    private fun Char.isLetterOrDigitCompat(): Boolean = isLetter() || isDigit()

    // Characters allowed at start of an IDENT (also used for path-ish macros)
    private fun Char.isIdentStart(): Boolean = isLetter() || this == '_' || this == '\\' || this == '/' || this == '.'

    // Characters allowed *inside* an IDENT
    private fun Char.isIdentPart(): Boolean = isLetterOrDigitCompat() || this == '_' || this == '\\' || this == '/' || this == '.'

}
