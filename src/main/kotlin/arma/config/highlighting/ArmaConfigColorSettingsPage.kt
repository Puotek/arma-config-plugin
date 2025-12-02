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
#include "file.hpp"
#include "includes\file.hpp"

#define SINGLE_LINE_DEFINE SINGLE_LINE_DEFINE
#define MULTI_LINE_DEFINE MULTI_LINE_DEFINE\
MULTI_LINE_DEFINE

//Single line comment
/* Block comment one line */
/*
Block
comment
multiline
*/

//todo works?
//fixme works as well?

class NoBodyClass;
class TestClass {
    indentifier = anotherIdentifer;
    array[] = {}; //inline comment
    arrayMultiline[] = {
        "test",
        "test"
        ,"test"
    }; //inline comment
    string = "hallo from the other siiide";
    math = 1 + 1 + (42 - 2);
    float = 2.20;
    number = 12;
    preprocessorThing = Q(path\to\file.paa);
    complexPreprocessor = Q(_target setObjectTextureGlobal [0, 'PATH(textures\ipsc_vertical.paa)'];);
    class NestedClass : extendClass {
        /*Another block comment*/
        array[] = {var, 1, 1e1, "", 42, 43};
        math = 1 + 69 * 1;
    };
    class TAG(preprocessorClass) : TAG(extensionAlso) {};
    thingy = 0;
    delete DeleteIsAKeywordTooClass;
    PREPROCESSOR_USED_FOR_PARAMS(something);
    JUST_A_PREPROCESSOR_THING;
    class NoBodyClass;
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
