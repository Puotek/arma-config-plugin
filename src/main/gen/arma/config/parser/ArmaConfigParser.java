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
  // COLON className
  public static boolean classExt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classExt")) return false;
    if (!nextTokenIs(b, COLON)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLON);
    r = r && className(b, l + 1);
    exit_section_(b, m, CLASS_EXT, r);
    return r;
  }

  /* ********************************************************** */
  // CLASS_KEYWORD IDENT classExt? SEMICOLON
  public static boolean classForwardDecl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classForwardDecl")) return false;
    if (!nextTokenIs(b, CLASS_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, CLASS_KEYWORD, IDENT);
    r = r && classForwardDecl_2(b, l + 1);
    r = r && consumeToken(b, SEMICOLON);
    exit_section_(b, m, CLASS_FORWARD_DECL, r);
    return r;
  }

  // classExt?
  private static boolean classForwardDecl_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classForwardDecl_2")) return false;
    classExt(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // IDENT | macroInvocation
  public static boolean className(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "className")) return false;
    if (!nextTokenIs(b, IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENT);
    if (!r) r = macroInvocation(b, l + 1);
    exit_section_(b, m, CLASS_NAME, r);
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
  // DELETE_KEYWORD IDENT SEMICOLON
  public static boolean deleteStmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "deleteStmt")) return false;
    if (!nextTokenIs(b, DELETE_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, DELETE_KEYWORD, IDENT, SEMICOLON);
    exit_section_(b, m, DELETE_STMT, r);
    return r;
  }

  /* ********************************************************** */
  // classDecl
  //                | classForwardDecl
  //                | assignment
  //                | deleteStmt
  //                | macroStmt
  //                | ';'
  static boolean item(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "item")) return false;
    boolean r;
    r = classDecl(b, l + 1);
    if (!r) r = classForwardDecl(b, l + 1);
    if (!r) r = assignment(b, l + 1);
    if (!r) r = deleteStmt(b, l + 1);
    if (!r) r = macroStmt(b, l + 1);
    if (!r) r = consumeToken(b, SEMICOLON);
    return r;
  }

  /* ********************************************************** */
  // macroInnerToken*
  public static boolean macroInner(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "macroInner")) return false;
    Marker m = enter_section_(b, l, _NONE_, MACRO_INNER, "<macro inner>");
    while (true) {
      int c = current_position_(b);
      if (!macroInnerToken(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "macroInner", c)) break;
    }
    exit_section_(b, l, m, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // IDENT
  //                   | NUMBER
  //                   | FLOAT
  //                   | STRING
  //                   | PREPROCESSOR
  //                   | LBRACE | RBRACE
  //                   | LBRACKET | RBRACKET
  //                   | COMMA
  //                   | EQUAL
  //                   | COLON
  //                   | LINE_COMMENT
  //                   | BLOCK_COMMENT
  public static boolean macroInnerToken(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "macroInnerToken")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, MACRO_INNER_TOKEN, "<macro inner token>");
    r = consumeToken(b, IDENT);
    if (!r) r = consumeToken(b, NUMBER);
    if (!r) r = consumeToken(b, FLOAT);
    if (!r) r = consumeToken(b, STRING);
    if (!r) r = consumeToken(b, PREPROCESSOR);
    if (!r) r = consumeToken(b, LBRACE);
    if (!r) r = consumeToken(b, RBRACE);
    if (!r) r = consumeToken(b, LBRACKET);
    if (!r) r = consumeToken(b, RBRACKET);
    if (!r) r = consumeToken(b, COMMA);
    if (!r) r = consumeToken(b, EQUAL);
    if (!r) r = consumeToken(b, COLON);
    if (!r) r = consumeToken(b, LINE_COMMENT);
    if (!r) r = consumeToken(b, BLOCK_COMMENT);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // IDENT LPAREN macroInner? RPAREN
  public static boolean macroInvocation(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "macroInvocation")) return false;
    if (!nextTokenIs(b, IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, IDENT, LPAREN);
    r = r && macroInvocation_2(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, MACRO_INVOCATION, r);
    return r;
  }

  // macroInner?
  private static boolean macroInvocation_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "macroInvocation_2")) return false;
    macroInner(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // macroInvocation SEMICOLON
  //             | IDENT SEMICOLON
  public static boolean macroStmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "macroStmt")) return false;
    if (!nextTokenIs(b, IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = macroStmt_0(b, l + 1);
    if (!r) r = parseTokens(b, 0, IDENT, SEMICOLON);
    exit_section_(b, m, MACRO_STMT, r);
    return r;
  }

  // macroInvocation SEMICOLON
  private static boolean macroStmt_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "macroStmt_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = macroInvocation(b, l + 1);
    r = r && consumeToken(b, SEMICOLON);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // IDENT
  //         | FLOAT
  //         | NUMBER
  //         | STRING
  //         | array
  //         | macroInvocation
  public static boolean value(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "value")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, VALUE, "<value>");
    r = consumeToken(b, IDENT);
    if (!r) r = consumeToken(b, FLOAT);
    if (!r) r = consumeToken(b, NUMBER);
    if (!r) r = consumeToken(b, STRING);
    if (!r) r = array(b, l + 1);
    if (!r) r = macroInvocation(b, l + 1);
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
