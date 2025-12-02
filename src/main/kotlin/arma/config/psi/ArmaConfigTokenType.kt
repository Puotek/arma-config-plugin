package arma.config.psi           // PSI package

import com.intellij.psi.tree.IElementType // Base class for token types
import arma.config.ArmaConfigLanguage     // Our language singleton

// Each instance represents a kind of token (e.g. IDENT, NUMBER, etc.),
// created by GrammarKit and used in ArmaConfigTypes.*
class ArmaConfigTokenType(debugName: String) : IElementType(debugName, ArmaConfigLanguage) {

    // Debug-friendly toString shown in logs and PSI Viewer
    override fun toString(): String = "ArmaConfigTokenType." + super.toString()
}
