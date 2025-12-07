package arma.config

import arma.config.parser.CfgParser   // Generated parser from GrammarKit (.bnf)
import arma.config.psi.CfgFile        // Our PSI file implementation
import arma.config.psi.CfgTypes       // Generated token/element type constants
import arma.config.psi.CfgTypes.STRING
import com.intellij.lang.ASTNode             // Nodes in parse tree
import com.intellij.lang.ParserDefinition    // Interface we implement
import com.intellij.lang.PsiParser           // Parser interface
import com.intellij.lexer.Lexer             // Lexer interface
import com.intellij.openapi.project.Project  // Not really used here, but required by signatures
import com.intellij.psi.FileViewProvider     // PSI ↔ document bridge
import com.intellij.psi.PsiElement           // Base PSI element type
import com.intellij.psi.PsiFile              // PSI file type
import com.intellij.psi.TokenType.WHITE_SPACE
import com.intellij.psi.tree.IFileElementType// Root node type for files
import com.intellij.psi.tree.TokenSet        // Groups of tokens
import com.intellij.psi.tree.TokenSet.create

class CfgParserDefinition : ParserDefinition {

    // Return a lexer instance for this language
    override fun createLexer(project: Project): Lexer = CfgLexer()

    // Return a parser instance for this language
    override fun createParser(project: Project): PsiParser = CfgParser()

    // Root file node type for this language
    override fun getFileNodeType(): IFileElementType = IFileElementType(CfgLanguage)

    // Tokens that are treated as whitespace
    override fun getWhitespaceTokens(): TokenSet = create(WHITE_SPACE)

    // Tokens treated as comments (used e.g. for code folding of comments, etc.)
    override fun getCommentTokens(): TokenSet = create(
        CfgTypes.LINE_COMMENT,
        CfgTypes.BLOCK_COMMENT,
        CfgTypes.PREPROCESSOR
    )

    // Tokens treated as string literals
    override fun getStringLiteralElements(): TokenSet = create(STRING)

    // Factory method to create a PSI element for a given AST node
    override fun createElement(node: ASTNode): PsiElement =
        CfgTypes.Factory.createElement(node)

    // Creates the PSI file root for this language, wrapping a FileViewProvider
    override fun createFile(viewProvider: FileViewProvider): PsiFile =
        CfgFile(viewProvider)

    // Defines whether spaces are required/forbidden/optional between tokens
    override fun spaceExistenceTypeBetweenTokens(
        left: ASTNode,
        right: ASTNode
    ): ParserDefinition.SpaceRequirements =
        ParserDefinition.SpaceRequirements.MAY
}
