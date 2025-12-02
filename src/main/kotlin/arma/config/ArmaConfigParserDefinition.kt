package arma.config

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import arma.config.parser.ArmaConfigParser
import arma.config.psi.ArmaConfigFile
import arma.config.psi.ArmaConfigTypes

class ArmaConfigParserDefinition : ParserDefinition {

    companion object {
        val FILE = IFileElementType(ArmaConfigLanguage)
        val WHITE_SPACES: TokenSet = TokenSet.create(TokenType.WHITE_SPACE)
    }

    override fun createLexer(project: Project): Lexer =
        ArmaConfigLexer()

    override fun createParser(project: Project): PsiParser =
        ArmaConfigParser()

    override fun getFileNodeType(): IFileElementType = FILE

    override fun getWhitespaceTokens(): TokenSet = WHITE_SPACES

    override fun getCommentTokens(): TokenSet = TokenSet.EMPTY

    override fun getStringLiteralElements(): TokenSet = TokenSet.EMPTY

    override fun createElement(node: ASTNode): PsiElement =
        ArmaConfigTypes.Factory.createElement(node)

    override fun createFile(viewProvider: FileViewProvider): PsiFile =
        ArmaConfigFile(viewProvider)

    override fun spaceExistenceTypeBetweenTokens(
        left: ASTNode,
        right: ASTNode
    ): ParserDefinition.SpaceRequirements =
        ParserDefinition.SpaceRequirements.MAY
}
