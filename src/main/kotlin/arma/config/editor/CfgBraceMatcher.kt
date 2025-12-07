package arma.config.editor

import arma.config.psi.CfgTypes
import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType

/**
 * Provides basic paired brace/paren behavior for Arma Config:
 *  - {} are treated as structural (code blocks)
 *  - () are paired but not structural (no auto-indent tricks)
 *  - [] are also paired for convenience (arrays)
 */
class CfgBraceMatcher : PairedBraceMatcher {

    private val pairs = arrayOf(
        // Structural code block
        BracePair(CfgTypes.LBRACE, CfgTypes.RBRACE, /* structural */ true),

        // Arrays
        BracePair(CfgTypes.LBRACKET, CfgTypes.RBRACKET, /* structural */ false),

        // Parentheses in macros etc.
        BracePair(CfgTypes.LPAREN, CfgTypes.RPAREN, /* structural */ false)
    )

    override fun getPairs(): Array<BracePair> = pairs

    override fun isPairedBracesAllowedBeforeType(
        lbraceType: IElementType, contextType: IElementType?
    ): Boolean {
        // Be permissive; allow closing brace before almost anything
        return true
    }

    override fun getCodeConstructStart(
        file: PsiFile?, openingBraceOffset: Int
    ): Int {
        // For now, treat the opening brace itself as the start of the construct
        return openingBraceOffset
    }
}
