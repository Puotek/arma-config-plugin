package arma.config.liveTemplates

import arma.config.ArmaConfigLanguage
import com.intellij.codeInsight.template.TemplateActionContext
import com.intellij.codeInsight.template.TemplateContextType

@Suppress("DialogTitleCapitalization")
class ArmaConfigTemplateContextType : TemplateContextType("Arma Config") {
    override fun isInContext(context: TemplateActionContext): Boolean {
        return context.file.language is ArmaConfigLanguage
    }
}
