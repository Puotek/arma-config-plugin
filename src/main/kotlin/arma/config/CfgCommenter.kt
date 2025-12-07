package arma.config

import com.intellij.lang.Commenter

class CfgCommenter : Commenter {

    // Line comments: used by Ctrl+/
    override fun getLineCommentPrefix(): String = "//"

    // Block comments: used by Ctrl+Shift+/
    override fun getBlockCommentPrefix(): String = "/*"

    override fun getBlockCommentSuffix(): String = "*/"

    // We don't support nested block comments specially
    override fun getCommentedBlockCommentPrefix(): String? = null
    override fun getCommentedBlockCommentSuffix(): String? = null
}
