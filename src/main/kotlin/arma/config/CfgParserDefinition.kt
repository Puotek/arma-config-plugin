package arma.config

import arma.config.parser.CfgParser
import arma.config.psi.CfgFile
import arma.config.psi.CfgTypes
import arma.config.psi.CfgTypes.STRING
import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType.WHITE_SPACE
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.tree.TokenSet.create

class CfgParserDefinition : ParserDefinition {
    override fun getFileNodeType(): IFileElementType = FILE
    override fun createLexer(project: Project): Lexer = CfgLexer()
    override fun createParser(project: Project): PsiParser = CfgParser()
    override fun createElement(node: ASTNode): PsiElement = CfgTypes.Factory.createElement(node)
    override fun createFile(viewProvider: FileViewProvider): PsiFile = CfgFile(viewProvider)
    override fun spaceExistenceTypeBetweenTokens(left: ASTNode, right: ASTNode): ParserDefinition.SpaceRequirements = ParserDefinition.SpaceRequirements.MAY
    override fun getWhitespaceTokens(): TokenSet = WHITESPACE_TOKENS
    override fun getStringLiteralElements(): TokenSet = STRING_LITERAL_ELEMENTS
    override fun getCommentTokens(): TokenSet = COMMENT_TOKENS
}

val FILE = IFileElementType(CfgLanguage)
val WHITESPACE_TOKENS = create(WHITE_SPACE)
val STRING_LITERAL_ELEMENTS = create(STRING)
val COMMENT_TOKENS = create(
    CfgTypes.LINE_COMMENT,
    CfgTypes.BLOCK_COMMENT,
    CfgTypes.PREPROCESSOR
)
