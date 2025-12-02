package arma.config.psi

import com.intellij.psi.tree.IElementType
import arma.config.ArmaConfigLanguage

class ArmaConfigTokenType(debugName: String) : IElementType(debugName, ArmaConfigLanguage) {
    override fun toString(): String = "ArmaConfigTokenType." + super.toString()
}
