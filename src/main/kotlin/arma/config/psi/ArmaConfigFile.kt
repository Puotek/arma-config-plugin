package arma.config.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.psi.FileViewProvider
import arma.config.ArmaConfigLanguage
import arma.config.ArmaConfigFileType

class ArmaConfigFile(viewProvider: FileViewProvider)
    : PsiFileBase(viewProvider, ArmaConfigLanguage) {

    override fun getFileType() = ArmaConfigFileType

    override fun toString(): String = "Arma Config File"
}
