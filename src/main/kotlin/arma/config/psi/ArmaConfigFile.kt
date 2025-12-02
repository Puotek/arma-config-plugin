package arma.config.psi              // PSI-related classes

import com.intellij.extapi.psi.PsiFileBase // Base convenience class for custom file PSI
import com.intellij.psi.FileViewProvider   // Bridges between text file and PSI
import arma.config.ArmaConfigLanguage      // Our language
import arma.config.ArmaConfigFileType      // Our file type

// This is the root PSI element for an Arma config file
class ArmaConfigFile(viewProvider: FileViewProvider)
    : PsiFileBase(viewProvider, ArmaConfigLanguage) {

    // Tells IntelliJ which FileType this PSI file corresponds to
    override fun getFileType() = ArmaConfigFileType

    // Used in UI (e.g. PSI Viewer)
    override fun toString(): String = "Arma Config File"
}
