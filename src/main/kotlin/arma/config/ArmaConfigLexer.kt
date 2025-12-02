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
        var i = if (tokenEnd == 0) startOffset else tokenEnd

        if (i >= endOffset) {
            tokenType = null
            tokenStart = endOffset
            tokenEnd = endOffset
            return
        }

        if (buffer[i].isWhitespace()) {
            tokenStart = i
            while (i < endOffset && buffer[i].isWhitespace()) {
                i++
            }
            tokenEnd = i
            tokenType = TokenType.WHITE_SPACE
            return
        }

        tokenStart = i
        val c = buffer[i]

        if (c.isLetter() || c == '_') {
            i++
            while (i < endOffset) {
                val ch = buffer[i]
                if (ch.isLetterOrDigit() || ch == '_') {
                    i++
                } else break
            }
            tokenEnd = i
            tokenType = ArmaConfigTypes.IDENT
            return
        }

        if (c.isDigit()) {
            i++
            while (i < endOffset && buffer[i].isDigit()) {
                i++
            }
            tokenEnd = i
            tokenType = ArmaConfigTypes.NUMBER
            return
        }

        if (c == '"') {
            i++
            while (i < endOffset) {
                val ch = buffer[i]
                if (ch == '\\') {
                    i += 2
                } else if (ch == '"') {
                    i++
                    break
                } else if (ch == '\n' || ch == '\r') {
                    break
                } else {
                    i++
                }
            }
            tokenEnd = i
            tokenType = ArmaConfigTypes.STRING
            return
        }

        tokenEnd = i + 1
        tokenType = TokenType.BAD_CHARACTER
    }

    private fun Char.isLetterOrDigit(): Boolean = isLetter() || isDigit()
}
