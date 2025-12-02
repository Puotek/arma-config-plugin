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

    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
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
        // skip whitespace
        var i = if (tokenEnd == 0) startOffset else tokenEnd
        while (i < endOffset && buffer[i].isWhitespace()) {
            i++
        }
        if (i >= endOffset) {
            tokenType = null
            tokenStart = endOffset
            tokenEnd = endOffset
            return
        }

        tokenStart = i
        val c = buffer[i]

        // identifiers: [A-Za-z_][A-Za-z0-9_]*
        if (c.isLetter() || c == '_') {
            i++
            while (i < endOffset && (buffer[i].isLetterOrDigit() || buffer[i] == '_')) {
                i++
            }
            val text = buffer.subSequence(tokenStart, i).toString()
            tokenEnd = i
            tokenType = when (text) {
                "class" -> ArmaConfigTypes.CLASS_KEYWORD
                else -> ArmaConfigTypes.IDENT
            }
            return
        }

        // numbers: \d+
        if (c.isDigit()) {
            i++
            while (i < endOffset && buffer[i].isDigit()) {
                i++
            }
            tokenEnd = i
            tokenType = ArmaConfigTypes.NUMBER
            return
        }

        // strings: " ... "
        if (c == '"') {
            i++ // consume opening quote
            var escaped = false
            while (i < endOffset) {
                val ch = buffer[i]
                if (escaped) {
                    escaped = false
                } else {
                    if (ch == '\\') {
                        escaped = true
                    } else if (ch == '"') {
                        i++ // consume closing quote
                        break
                    } else if (ch == '\n' || ch == '\r') {
                        // break on newline – invalid string, let parser handle it
                        break
                    }
                }
                i++
            }
            tokenEnd = i
            tokenType = ArmaConfigTypes.STRING
            return
        }

        // single-character tokens
        tokenEnd = i + 1
        tokenType = when (c) {
            '{' -> ArmaConfigTypes.LBRACE
            '}' -> ArmaConfigTypes.RBRACE
            '=' -> ArmaConfigTypes.EQUAL
            ';' -> ArmaConfigTypes.SEMICOLON
            else -> TokenType.BAD_CHARACTER
        }
    }

    private fun Char.isLetterOrDigit(): Boolean = isLetter() || isDigit()
}
