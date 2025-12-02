package arma.config

import com.intellij.lexer.LexerBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import arma.config.psi.ArmaConfigTypes

class ArmaConfigLexer : LexerBase() {

    private var buffer: CharSequence = ""
    private var startOffset: Int = 0
    private var endOffset: Int = 0

    private var tokenStart: Int = 0
    private var tokenEnd: Int = 0
    private var tokenType: IElementType? = null

    override fun start(
        buffer: CharSequence,
        startOffset: Int,
        endOffset: Int,
        initialState: Int
    ) {
        this.buffer = buffer
        this.startOffset = startOffset
        this.endOffset = endOffset

        this.tokenStart = startOffset
        this.tokenEnd = startOffset
        this.tokenType = null

        advance()
    }

    override fun getState(): Int = 0

    override fun getTokenType(): IElementType? = tokenType

    override fun getTokenStart(): Int = tokenStart

    override fun getTokenEnd(): Int = tokenEnd

    override fun getBufferSequence(): CharSequence = buffer

    override fun getBufferEnd(): Int = endOffset

    override fun advance() {
        var i = tokenEnd
        if (i < startOffset) i = startOffset

        // EOF
        if (i >= endOffset) {
            tokenType = null
            tokenStart = endOffset
            tokenEnd = endOffset
            return
        }

        val c = buffer[i]

        // WHITESPACE
        if (c.isWhitespace()) {
            tokenStart = i
            var j = i + 1
            while (j < endOffset && buffer[j].isWhitespace()) {
                j++
            }
            tokenEnd = j
            tokenType = TokenType.WHITE_SPACE
            return
        }

        tokenStart = i

        // PREPROCESSOR: #... with \-continued lines
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
                        // continuation: skip this newline and keep going
                        j++
                        // handle CRLF
                        if (ch == '\r' && j < endOffset && buffer[j] == '\n') {
                            j++
                        }
                        continue
                    } else {
                        // real end of preprocessor directive
                        break
                    }
                }

                j++
            }
            tokenEnd = j
            tokenType = ArmaConfigTypes.PREPROCESSOR
            return
        }

        // COMMENTS: //... or /* ... */
        if (c == '/' && i + 1 < endOffset) {
            val next = buffer[i + 1]

            // line comment
            if (next == '/') {
                var j = i + 2
                while (j < endOffset) {
                    val ch = buffer[j]
                    if (ch == '\n' || ch == '\r') break
                    j++
                }
                tokenEnd = j
                tokenType = ArmaConfigTypes.LINE_COMMENT
                return
            }

            // block comment
            if (next == '*') {
                var j = i + 2
                while (j < endOffset - 1) {
                    if (buffer[j] == '*' && buffer[j + 1] == '/') {
                        j += 2
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

        // IDENT or keyword
        if (c.isLetter() || c == '_') {
            var j = i + 1
            while (j < endOffset) {
                val ch = buffer[j]
                if (ch.isLetterOrDigit() || ch == '_') {
                    j++
                } else break
            }
            tokenEnd = j
            val text = buffer.subSequence(tokenStart, tokenEnd).toString()
            tokenType = when (text) {
                "class" -> ArmaConfigTypes.CLASS_KEYWORD
                else    -> ArmaConfigTypes.IDENT
            }
            return
        }

        // NUMBER
        if (c.isDigit()) {
            var j = i + 1
            while (j < endOffset && buffer[j].isDigit()) {
                j++
            }
            tokenEnd = j
            tokenType = ArmaConfigTypes.NUMBER
            return
        }

        // STRING: " ... "
        if (c == '"') {
            var j = i + 1
            while (j < endOffset) {
                val ch = buffer[j]
                if (ch == '\\') {
                    // skip escaped char (\" etc.)
                    j += 2
                } else if (ch == '"') {
                    j++  // include closing quote
                    break
                } else if (ch == '\n' || ch == '\r') {
                    // unterminated string: stop at newline
                    break
                } else {
                    j++
                }
            }
            if (j > endOffset) j = endOffset
            tokenEnd = j
            tokenType = ArmaConfigTypes.STRING
            return
        }

        // Single-character tokens
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
            else -> TokenType.BAD_CHARACTER
        }
    }

    private fun Char.isLetterOrDigit(): Boolean = isLetter() || isDigit()
}
