package arma.config.psi              // PSI-related classes

import com.intellij.extapi.psi.PsiFileBase // Base convenience class for custom file PSI
import com.intellij.psi.FileViewProvider   // Bridges between text file and PSI
import arma.config.CfgLanguage      // Our language
import arma.config.CfgFileType      // Our file type
import com.intellij.openapi.fileTypes.FileType
import javax.swing.Icon
import com.intellij.icons.AllIcons

// This is the root PSI element for an Arma config file
class CfgFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, CfgLanguage) {

    // Tells IntelliJ which FileType this PSI file corresponds to
    override fun getFileType(): FileType = CfgFileType

    // Used in UI (e.g. PSI Viewer)
    override fun toString(): String = "Arma Config File"

    override fun getIcon(flags: Int): Icon? {
        val name = name.lowercase()

        return when {
            name == "config.cpp" -> AllIcons.Nodes.Class      // special icon
            else -> AllIcons.Nodes.Enum
        }
    }
}
