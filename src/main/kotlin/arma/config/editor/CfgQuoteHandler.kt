package arma.config.editor

import arma.config.psi.CfgTypes
import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler

/**
 * Enables automatic pairing of "..." in Arma Config files.
 */
class CfgQuoteHandler : SimpleTokenSetQuoteHandler(
    CfgTypes.STRING
)
