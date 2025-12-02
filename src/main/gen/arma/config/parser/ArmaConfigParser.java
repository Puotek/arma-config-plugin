// This is a generated file. Not intended for manual editing.
package arma.config.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static arma.config.psi.ArmaConfigTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class ArmaConfigParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, null);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    r = parse_root_(t, b);
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b) {
    return parse_root_(t, b, 0);
  }

  static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return configFile(b, l + 1);
  }

  /* ********************************************************** */
  // IDENT EQUAL value SEMICOLON
  public static boolean assignment(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assignment")) return false;
    if (!nextTokenIs(b, IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, IDENT, EQUAL);
    r = r && value(b, l + 1);
    r = r && consumeToken(b, SEMICOLON);
    exit_section_(b, m, ASSIGNMENT, r);
    return r;
  }

  /* ********************************************************** */
  // CLASS_KEYWORD IDENT LBRACE item* RBRACE SEMICOLON?
  public static boolean classDecl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classDecl")) return false;
    if (!nextTokenIs(b, CLASS_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, CLASS_KEYWORD, IDENT, LBRACE);
    r = r && classDecl_3(b, l + 1);
    r = r && consumeToken(b, RBRACE);
    r = r && classDecl_5(b, l + 1);
    exit_section_(b, m, CLASS_DECL, r);
    return r;
  }

  // item*
  private static boolean classDecl_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classDecl_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!item(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "classDecl_3", c)) break;
    }
    return true;
  }

  // SEMICOLON?
  private static boolean classDecl_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classDecl_5")) return false;
    consumeToken(b, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // item*
  static boolean configFile(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "configFile")) return false;
    while (true) {
      int c = current_position_(b);
      if (!item(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "configFile", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // classDecl | assignment | ';'
  static boolean item(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "item")) return false;
    boolean r;
    r = classDecl(b, l + 1);
    if (!r) r = assignment(b, l + 1);
    if (!r) r = consumeToken(b, SEMICOLON);
    return r;
  }

  /* ********************************************************** */
  // IDENT | NUMBER | STRING
  public static boolean value(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "value")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, VALUE, "<value>");
    r = consumeToken(b, IDENT);
    if (!r) r = consumeToken(b, NUMBER);
    if (!r) r = consumeToken(b, STRING);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

}
