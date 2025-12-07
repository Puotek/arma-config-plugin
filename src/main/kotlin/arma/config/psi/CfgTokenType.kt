package arma.config.psi           // PSI package

import com.intellij.psi.tree.IElementType // Base class for token types
import arma.config.CfgLanguage     // Our language singleton

// Each instance represents a kind of token (e.g. IDENT, NUMBER, etc.),
// created by GrammarKit and used in CfgTypes.*
class CfgTokenType(debugName: String) : IElementType(debugName, CfgLanguage) {

    // Debug-friendly toString shown in logs and PSI Viewer
    override fun toString(): String = "CfgTokenType." + super.toString()
}
