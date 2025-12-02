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
  // LBRACE valueList? RBRACE
  public static boolean array(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array")) return false;
    if (!nextTokenIs(b, LBRACE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LBRACE);
    r = r && array_1(b, l + 1);
    r = r && consumeToken(b, RBRACE);
    exit_section_(b, m, ARRAY, r);
    return r;
  }

  // valueList?
  private static boolean array_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_1")) return false;
    valueList(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // LBRACKET RBRACKET
  public static boolean arraySuffix(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arraySuffix")) return false;
    if (!nextTokenIs(b, LBRACKET)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, LBRACKET, RBRACKET);
    exit_section_(b, m, ARRAY_SUFFIX, r);
    return r;
  }

  /* ********************************************************** */
  // IDENT arraySuffix? EQUAL value SEMICOLON
  public static boolean assignment(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assignment")) return false;
    if (!nextTokenIs(b, IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENT);
    r = r && assignment_1(b, l + 1);
    r = r && consumeToken(b, EQUAL);
    r = r && value(b, l + 1);
    r = r && consumeToken(b, SEMICOLON);
    exit_section_(b, m, ASSIGNMENT, r);
    return r;
  }

  // arraySuffix?
  private static boolean assignment_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assignment_1")) return false;
    arraySuffix(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // CLASS_KEYWORD IDENT classExt? LBRACE item* RBRACE SEMICOLON?
  public static boolean classDecl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classDecl")) return false;
    if (!nextTokenIs(b, CLASS_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, CLASS_KEYWORD, IDENT);
    r = r && classDecl_2(b, l + 1);
    r = r && consumeToken(b, LBRACE);
    r = r && classDecl_4(b, l + 1);
    r = r && consumeToken(b, RBRACE);
    r = r && classDecl_6(b, l + 1);
    exit_section_(b, m, CLASS_DECL, r);
    return r;
  }

  // classExt?
  private static boolean classDecl_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classDecl_2")) return false;
    classExt(b, l + 1);
    return true;
  }

  // item*
  private static boolean classDecl_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classDecl_4")) return false;
    while (true) {
      int c = current_position_(b);
      if (!item(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "classDecl_4", c)) break;
    }
    return true;
  }

  // SEMICOLON?
  private static boolean classDecl_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classDecl_6")) return false;
    consumeToken(b, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // COLON IDENT
  public static boolean classExt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classExt")) return false;
    if (!nextTokenIs(b, COLON)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, COLON, IDENT);
    exit_section_(b, m, CLASS_EXT, r);
    return r;
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
  // IDENT | NUMBER | STRING | array
  public static boolean value(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "value")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, VALUE, "<value>");
    r = consumeToken(b, IDENT);
    if (!r) r = consumeToken(b, NUMBER);
    if (!r) r = consumeToken(b, STRING);
    if (!r) r = array(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // value (COMMA value)*
  public static boolean valueList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "valueList")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, VALUE_LIST, "<value list>");
    r = value(b, l + 1);
    r = r && valueList_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (COMMA value)*
  private static boolean valueList_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "valueList_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!valueList_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "valueList_1", c)) break;
    }
    return true;
  }

  // COMMA value
  private static boolean valueList_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "valueList_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && value(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

}
