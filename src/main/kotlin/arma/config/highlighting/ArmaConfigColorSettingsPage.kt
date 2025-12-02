package arma.config.highlighting

import arma.config.ArmaConfigLanguage
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import javax.swing.Icon

class ArmaConfigColorSettingsPage : ColorSettingsPage {

    // Name shown in Settings → Editor → Color Scheme → Arma Config
    override fun getDisplayName(): String = "Arma Config"

    override fun getIcon(): Icon? = null // or your file type icon if you have one

    // Highlighter used to render the demo text preview
    override fun getHighlighter(): SyntaxHighlighter =
        SyntaxHighlighterFactory.getSyntaxHighlighter(ArmaConfigLanguage, null, null)

    // Sample text shown in the preview pane inside color settings
    override fun getDemoText(): String = """
        // Comment
        #include "define.hpp"
        #define MY_MACRO 1
        
        class CfgPatches {
            class myMod {
                requiredVersion = 2.20;
                requiredAddons[] = {"cba_main", "ace_common"};
            };
        };
    """.trimIndent()

    // Which named attributes the user can configure in the Color Scheme UI
    override fun getAttributeDescriptors(): Array<AttributesDescriptor> = arrayOf(
        AttributesDescriptor("Keyword", ArmaConfigSyntaxHighlighter.KEYWORD),
        AttributesDescriptor("String", ArmaConfigSyntaxHighlighter.STRING),
        AttributesDescriptor("Number", ArmaConfigSyntaxHighlighter.NUMBER),
        AttributesDescriptor("Braces / Brackets", ArmaConfigSyntaxHighlighter.BRACES),
        AttributesDescriptor("Operators / Punctuation", ArmaConfigSyntaxHighlighter.OPERATOR),
        AttributesDescriptor("Comment", ArmaConfigSyntaxHighlighter.COMMENT),
        AttributesDescriptor("Preprocessor Keyword", ArmaConfigSyntaxHighlighter.PREPROCESSOR),
        AttributesDescriptor("Bad character", ArmaConfigSyntaxHighlighter.BAD_CHAR),
    )

    // No standalone colors (we only define attribute keys)
    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY

    // We don't use <tag>…</tag> markers in demoText for custom attributes
    override fun getAdditionalHighlightingTagToDescriptorMap(): Map<String, TextAttributesKey>? = null
}
