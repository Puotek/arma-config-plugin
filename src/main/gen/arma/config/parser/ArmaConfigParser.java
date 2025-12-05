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
    return root(b, l + 1);
  }

  /* ********************************************************** */
  // identifier LBRACKET RBRACKET PLUS? EQUAL arrayBody SEMICOLON
  public static boolean arrayBlock(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arrayBlock")) return false;
    if (!nextTokenIs(b, TEXT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ARRAY_BLOCK, null);
    r = identifier(b, l + 1);
    r = r && consumeTokens(b, 2, LBRACKET, RBRACKET);
    p = r; // pin = 3
    r = r && report_error_(b, arrayBlock_3(b, l + 1));
    r = p && report_error_(b, consumeToken(b, EQUAL)) && r;
    r = p && report_error_(b, arrayBody(b, l + 1)) && r;
    r = p && consumeToken(b, SEMICOLON) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // PLUS?
  private static boolean arrayBlock_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arrayBlock_3")) return false;
    consumeToken(b, PLUS);
    return true;
  }

  /* ********************************************************** */
  // LBRACE (arrayElement (COMMA arrayElement)*)? RBRACE
  public static boolean arrayBody(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arrayBody")) return false;
    if (!nextTokenIs(b, LBRACE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LBRACE);
    r = r && arrayBody_1(b, l + 1);
    r = r && consumeToken(b, RBRACE);
    exit_section_(b, m, ARRAY_BODY, r);
    return r;
  }

  // (arrayElement (COMMA arrayElement)*)?
  private static boolean arrayBody_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arrayBody_1")) return false;
    arrayBody_1_0(b, l + 1);
    return true;
  }

  // arrayElement (COMMA arrayElement)*
  private static boolean arrayBody_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arrayBody_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = arrayElement(b, l + 1);
    r = r && arrayBody_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA arrayElement)*
  private static boolean arrayBody_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arrayBody_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!arrayBody_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "arrayBody_1_0_1", c)) break;
    }
    return true;
  }

  // COMMA arrayElement
  private static boolean arrayBody_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arrayBody_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && arrayElement(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // parameterValue | arrayBody
  static boolean arrayElement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arrayElement")) return false;
    boolean r;
    r = parameterValue(b, l + 1);
    if (!r) r = arrayBody(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // CLASS_KEYWORD identifier classExtension? classBody? SEMICOLON
  public static boolean classBlock(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classBlock")) return false;
    if (!nextTokenIs(b, CLASS_KEYWORD)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CLASS_BLOCK, null);
    r = consumeToken(b, CLASS_KEYWORD);
    p = r; // pin = 1
    r = r && report_error_(b, identifier(b, l + 1));
    r = p && report_error_(b, classBlock_2(b, l + 1)) && r;
    r = p && report_error_(b, classBlock_3(b, l + 1)) && r;
    r = p && consumeToken(b, SEMICOLON) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // classExtension?
  private static boolean classBlock_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classBlock_2")) return false;
    classExtension(b, l + 1);
    return true;
  }

  // classBody?
  private static boolean classBlock_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classBlock_3")) return false;
    classBody(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // LBRACE classItem* RBRACE
  public static boolean classBody(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classBody")) return false;
    if (!nextTokenIs(b, LBRACE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CLASS_BODY, null);
    r = consumeToken(b, LBRACE);
    p = r; // pin = 1
    r = r && report_error_(b, classBody_1(b, l + 1));
    r = p && consumeToken(b, RBRACE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // classItem*
  private static boolean classBody_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classBody_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!classItem(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "classBody_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // COLON identifier
  public static boolean classExtension(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classExtension")) return false;
    if (!nextTokenIs(b, COLON)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLON);
    r = r && identifier(b, l + 1);
    exit_section_(b, m, CLASS_EXTENSION, r);
    return r;
  }

  /* ********************************************************** */
  // parameterBlock
  //                     | arrayBlock
  //                     | item
  static boolean classItem(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classItem")) return false;
    boolean r;
    r = parameterBlock(b, l + 1);
    if (!r) r = arrayBlock(b, l + 1);
    if (!r) r = item(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // DELETE_KEYWORD identifier SEMICOLON
  public static boolean deleteBlock(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "deleteBlock")) return false;
    if (!nextTokenIs(b, DELETE_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DELETE_KEYWORD);
    r = r && identifier(b, l + 1);
    r = r && consumeToken(b, SEMICOLON);
    exit_section_(b, m, DELETE_BLOCK, r);
    return r;
  }

  /* ********************************************************** */
  // macroConcat (DOUBLE_HASH macroConcat)*
  public static boolean identifier(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "identifier")) return false;
    if (!nextTokenIs(b, TEXT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = macroConcat(b, l + 1);
    r = r && identifier_1(b, l + 1);
    exit_section_(b, m, IDENTIFIER, r);
    return r;
  }

  // (DOUBLE_HASH macroConcat)*
  private static boolean identifier_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "identifier_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!identifier_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "identifier_1", c)) break;
    }
    return true;
  }

  // DOUBLE_HASH macroConcat
  private static boolean identifier_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "identifier_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DOUBLE_HASH);
    r = r && macroConcat(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // identifier SEMICOLON
  //                | classBlock
  //                | deleteBlock
  //                | PREPROCESSOR
  //                | LINE_COMMENT
  //                | BLOCK_COMMENT
  static boolean item(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "item")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = item_0(b, l + 1);
    if (!r) r = classBlock(b, l + 1);
    if (!r) r = deleteBlock(b, l + 1);
    if (!r) r = consumeToken(b, PREPROCESSOR);
    if (!r) r = consumeToken(b, LINE_COMMENT);
    if (!r) r = consumeToken(b, BLOCK_COMMENT);
    exit_section_(b, m, null, r);
    return r;
  }

  // identifier SEMICOLON
  private static boolean item_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "item_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = identifier(b, l + 1);
    r = r && consumeToken(b, SEMICOLON);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // macroElement | LPAREN macroAtom+ RPAREN
  static boolean macroAtom(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "macroAtom")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = macroElement(b, l + 1);
    if (!r) r = macroAtom_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // LPAREN macroAtom+ RPAREN
  private static boolean macroAtom_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "macroAtom_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAREN);
    r = r && macroAtom_1_1(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  // macroAtom+
  private static boolean macroAtom_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "macroAtom_1_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = macroAtom(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!macroAtom(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "macroAtom_1_1", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // TEXT LPAREN macroAtom+ RPAREN
  static boolean macroBody(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "macroBody")) return false;
    if (!nextTokenIs(b, TEXT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, TEXT, LPAREN);
    r = r && macroBody_2(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  // macroAtom+
  private static boolean macroBody_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "macroBody_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = macroAtom(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!macroAtom(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "macroBody_2", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // macroBody | TEXT
  static boolean macroConcat(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "macroConcat")) return false;
    if (!nextTokenIs(b, TEXT)) return false;
    boolean r;
    r = macroBody(b, l + 1);
    if (!r) r = consumeToken(b, TEXT);
    return r;
  }

  /* ********************************************************** */
  // LBRACE
  //                        | RBRACE
  //                        | LBRACKET
  //                        | RBRACKET
  //                        | EQUAL
  //                        | SEMICOLON
  //                        | COLON
  //                        | PLUS
  //                        | MINUS
  //                        | STAR
  //                        | SLASH
  //                        | PERCENT
  //                        | CARET
  //                        | AMPERSAND
  //                        | GT
  //                        | LT
  //                        | EXCL
  //                        | SINGLE_HASH
  //                        | DOUBLE_HASH
  //                        | CLASS_KEYWORD
  //                        | DELETE_KEYWORD
  //                        | MIN_KEYWORD
  //                        | MAX_KEYWORD
  //                        | BLOCK_COMMENT
  //                        | TEXT
  //                        | FLOAT
  //                        | NUMBER
  //                        | STRING
  //                        | SINGLE_QUOTE
  //                        | COMMA
  static boolean macroElement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "macroElement")) return false;
    boolean r;
    r = consumeToken(b, LBRACE);
    if (!r) r = consumeToken(b, RBRACE);
    if (!r) r = consumeToken(b, LBRACKET);
    if (!r) r = consumeToken(b, RBRACKET);
    if (!r) r = consumeToken(b, EQUAL);
    if (!r) r = consumeToken(b, SEMICOLON);
    if (!r) r = consumeToken(b, COLON);
    if (!r) r = consumeToken(b, PLUS);
    if (!r) r = consumeToken(b, MINUS);
    if (!r) r = consumeToken(b, STAR);
    if (!r) r = consumeToken(b, SLASH);
    if (!r) r = consumeToken(b, PERCENT);
    if (!r) r = consumeToken(b, CARET);
    if (!r) r = consumeToken(b, AMPERSAND);
    if (!r) r = consumeToken(b, GT);
    if (!r) r = consumeToken(b, LT);
    if (!r) r = consumeToken(b, EXCL);
    if (!r) r = consumeToken(b, SINGLE_HASH);
    if (!r) r = consumeToken(b, DOUBLE_HASH);
    if (!r) r = consumeToken(b, CLASS_KEYWORD);
    if (!r) r = consumeToken(b, DELETE_KEYWORD);
    if (!r) r = consumeToken(b, MIN_KEYWORD);
    if (!r) r = consumeToken(b, MAX_KEYWORD);
    if (!r) r = consumeToken(b, BLOCK_COMMENT);
    if (!r) r = consumeToken(b, TEXT);
    if (!r) r = consumeToken(b, FLOAT);
    if (!r) r = consumeToken(b, NUMBER);
    if (!r) r = consumeToken(b, STRING);
    if (!r) r = consumeToken(b, SINGLE_QUOTE);
    if (!r) r = consumeToken(b, COMMA);
    return r;
  }

  /* ********************************************************** */
  // MINUS? mathElement (mathOperator mathElement)*
  public static boolean mathBlock(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mathBlock")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, MATH_BLOCK, "<math block>");
    r = mathBlock_0(b, l + 1);
    r = r && mathElement(b, l + 1);
    r = r && mathBlock_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // MINUS?
  private static boolean mathBlock_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mathBlock_0")) return false;
    consumeToken(b, MINUS);
    return true;
  }

  // (mathOperator mathElement)*
  private static boolean mathBlock_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mathBlock_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!mathBlock_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "mathBlock_2", c)) break;
    }
    return true;
  }

  // mathOperator mathElement
  private static boolean mathBlock_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mathBlock_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = mathOperator(b, l + 1);
    r = r && mathElement(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // TEXT
  //               | NUMBER
  //               | FLOAT
  //               | LPAREN mathBlock RPAREN
  public static boolean mathElement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mathElement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, MATH_ELEMENT, "<math element>");
    r = consumeToken(b, TEXT);
    if (!r) r = consumeToken(b, NUMBER);
    if (!r) r = consumeToken(b, FLOAT);
    if (!r) r = mathElement_3(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // LPAREN mathBlock RPAREN
  private static boolean mathElement_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mathElement_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAREN);
    r = r && mathBlock(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // PLUS
  //                | MINUS
  //                | SLASH
  //                | STAR
  //                | PERCENT
  //                | MIN_KEYWORD
  //                | MAX_KEYWORD
  //                | CARET
  //                | GT
  //                | LT
  public static boolean mathOperator(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mathOperator")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, MATH_OPERATOR, "<math operator>");
    r = consumeToken(b, PLUS);
    if (!r) r = consumeToken(b, MINUS);
    if (!r) r = consumeToken(b, SLASH);
    if (!r) r = consumeToken(b, STAR);
    if (!r) r = consumeToken(b, PERCENT);
    if (!r) r = consumeToken(b, MIN_KEYWORD);
    if (!r) r = consumeToken(b, MAX_KEYWORD);
    if (!r) r = consumeToken(b, CARET);
    if (!r) r = consumeToken(b, GT);
    if (!r) r = consumeToken(b, LT);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // identifier EQUAL parameterValue SEMICOLON
  public static boolean parameterBlock(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameterBlock")) return false;
    if (!nextTokenIs(b, TEXT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, PARAMETER_BLOCK, null);
    r = identifier(b, l + 1);
    r = r && consumeToken(b, EQUAL);
    p = r; // pin = 2
    r = r && report_error_(b, parameterValue(b, l + 1));
    r = p && consumeToken(b, SEMICOLON) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // identifier
  //                  | mathBlock
  //                  | STRING
  //                  | SINGLE_QUOTE
  //                  | NUMBER
  //                  | FLOAT
  public static boolean parameterValue(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameterValue")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PARAMETER_VALUE, "<parameter value>");
    r = identifier(b, l + 1);
    if (!r) r = mathBlock(b, l + 1);
    if (!r) r = consumeToken(b, STRING);
    if (!r) r = consumeToken(b, SINGLE_QUOTE);
    if (!r) r = consumeToken(b, NUMBER);
    if (!r) r = consumeToken(b, FLOAT);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // item*
  static boolean root(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "root")) return false;
    while (true) {
      int c = current_position_(b);
      if (!item(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "root", c)) break;
    }
    return true;
  }

}
