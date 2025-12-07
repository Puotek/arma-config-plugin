package arma.config.liveTemplates

import arma.config.CfgLanguage
import com.intellij.codeInsight.template.TemplateActionContext
import com.intellij.codeInsight.template.TemplateContextType

@Suppress("DialogTitleCapitalization")
class CfgTemplateContextType : TemplateContextType("Arma Config") {
    override fun isInContext(context: TemplateActionContext): Boolean {
        return context.file.language is CfgLanguage
    }
}
