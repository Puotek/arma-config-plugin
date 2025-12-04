package arma.config

import com.intellij.lexer.LexerBase          // Base class for implementing your own lexer
import com.intellij.psi.TokenType            // Contains generic token types like WHITE_SPACE, BAD_CHARACTER
import com.intellij.psi.tree.IElementType   // Base type for all token types
import arma.config.psi.ArmaConfigTypes      // Generated constants for token/element types

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
        // Start lexing from the end of the previous token
        var i = tokenEnd
        if (i < startOffset) i = startOffset

        // EOF: if we've reached or passed endOffset, no more tokens
        if (i >= endOffset) {
            tokenType = null
            tokenStart = endOffset
            tokenEnd = endOffset
            return
        }

        // Current char to examine
        val c = buffer[i]

        // WHITESPACE token
        if (c.isWhitespace()) {
            tokenStart = i
            var j = i + 1
            // Group consecutive whitespace characters into one WHITE_SPACE token
            while (j < endOffset && buffer[j].isWhitespace()) {
                j++
            }
            tokenEnd = j
            tokenType = TokenType.WHITE_SPACE
            return
        }

        // For all non-whitespace tokens, we start at i
        tokenStart = i

        // PREPROCESSOR: lines beginning with '#' (with '\' continuation)
        if (c == '#') {
            var j = i + 1
            while (j < endOffset) {
                val ch = buffer[j]

                if (ch == '\n' || ch == '\r') {
                    // find last non-whitespace before this newline
                    var k = j - 1
                    while (k >= i && buffer[k].isWhitespace()) {
                        k--
                    }
                    val lastNonWs = if (k >= i) buffer[k] else '\u0000'

                    if (lastNonWs == '\\') {
                        // Continuation line: skip newline and continue reading
                        j++
                        // If we had CRLF, skip both
                        if (ch == '\r' && j < endOffset && buffer[j] == '\n') {
                            j++
                        }
                        // Continue scanning the directive body
                        continue
                    } else {
                        // Real end of preprocessor directive
                        break
                    }
                }

                j++
            }
            tokenEnd = j
            tokenType = ArmaConfigTypes.PREPROCESSOR
            return
        }

        // COMMENTS: either // line comment or /* block comment */
        if (c == '/' && i + 1 < endOffset) {
            val next = buffer[i + 1]

            // line comment: // ...
            if (next == '/') {
                var j = i + 2
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
                var j = i + 2
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
        if (c.isIdentStart()) {
            var j = i + 1
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
                else -> ArmaConfigTypes.IDENT
            }
            return
        }

        // NUMBER, FLOAT, or identifier starting with a digit
        // e.g. "30Rnd_556x45_Stanag" should be a single IDENT, not NUMBER("30") + IDENT("Rnd_...")
        if (c.isDigit()) {
            var j = i
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
                    !seenExponent && (ch == 'e' || ch == 'E') && j > i -> {
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
                    else -> ArmaConfigTypes.IDENT
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
        if (c == '"') {
            var j = i + 1

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
        if (c == '\'') {
            val start = i
            var j = i + 1
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
                tokenStart = start
                tokenEnd = start + 1
                tokenType = TokenType.BAD_CHARACTER
                return
            }

            tokenStart = start
            tokenEnd = lastQuote + 1 // include the closing '
            tokenType = ArmaConfigTypes.SINGLE_QUOTE_BLOCK_TOKEN
            return
        }

        // Single-character punctuation tokens
        tokenEnd = i + 1
        tokenType = when (c) {
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
            '<' -> ArmaConfigTypes.GT
            '>' -> ArmaConfigTypes.LT
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
