package arma.config.editor

import arma.config.psi.ArmaConfigTypes
import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler

/**
 * Enables automatic pairing of "..." in Arma Config files.
 */
class ArmaConfigQuoteHandler : SimpleTokenSetQuoteHandler(
    ArmaConfigTypes.STRING
)
