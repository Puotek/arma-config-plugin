// This is a generated file. Not intended for manual editing.
package arma.config.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import arma.config.psi.impl.*;

public interface ArmaConfigTypes {

  IElementType ARRAY = new ArmaConfigElementType("ARRAY");
  IElementType ARRAY_SUFFIX = new ArmaConfigElementType("ARRAY_SUFFIX");
  IElementType ASSIGNMENT = new ArmaConfigElementType("ASSIGNMENT");
  IElementType CLASS_DECL = new ArmaConfigElementType("CLASS_DECL");
  IElementType CLASS_EXT = new ArmaConfigElementType("CLASS_EXT");
  IElementType CLASS_FORWARD_DECL = new ArmaConfigElementType("CLASS_FORWARD_DECL");
  IElementType CLASS_IDENT = new ArmaConfigElementType("CLASS_IDENT");
  IElementType CLASS_NAME = new ArmaConfigElementType("CLASS_NAME");
  IElementType DELETE_STMT = new ArmaConfigElementType("DELETE_STMT");
  IElementType EXPR = new ArmaConfigElementType("EXPR");
  IElementType EXPR_ADD = new ArmaConfigElementType("EXPR_ADD");
  IElementType EXPR_MUL = new ArmaConfigElementType("EXPR_MUL");
  IElementType EXPR_POWER = new ArmaConfigElementType("EXPR_POWER");
  IElementType EXPR_UNARY = new ArmaConfigElementType("EXPR_UNARY");
  IElementType MACRO_ATOM = new ArmaConfigElementType("MACRO_ATOM");
  IElementType MACRO_INNER = new ArmaConfigElementType("MACRO_INNER");
  IElementType MACRO_INNER_TOKEN = new ArmaConfigElementType("MACRO_INNER_TOKEN");
  IElementType MACRO_INVOCATION = new ArmaConfigElementType("MACRO_INVOCATION");
  IElementType MACRO_STMT = new ArmaConfigElementType("MACRO_STMT");
  IElementType PREPROC_VALUE = new ArmaConfigElementType("PREPROC_VALUE");
  IElementType PREPROC_VALUE_TOKEN = new ArmaConfigElementType("PREPROC_VALUE_TOKEN");
  IElementType PRIMARY = new ArmaConfigElementType("PRIMARY");
  IElementType SINGLE_QUOTE_BLOCK = new ArmaConfigElementType("SINGLE_QUOTE_BLOCK");
  IElementType VALUE = new ArmaConfigElementType("VALUE");
  IElementType VALUE_LIST = new ArmaConfigElementType("VALUE_LIST");

  IElementType BLOCK_COMMENT = new ArmaConfigTokenType("regex:/\\\\*([^*]|\\\\*+[^*/])*\\\\*+/");
  IElementType CARET = new ArmaConfigTokenType("^");
  IElementType CLASS_KEYWORD = new ArmaConfigTokenType("class");
  IElementType COLON = new ArmaConfigTokenType(":");
  IElementType COMMA = new ArmaConfigTokenType(",");
  IElementType DELETE_KEYWORD = new ArmaConfigTokenType("delete");
  IElementType EQUAL = new ArmaConfigTokenType("=");
  IElementType EXCL = new ArmaConfigTokenType("!");
  IElementType FLOAT = new ArmaConfigTokenType("regex:\\\\d+\\\\.\\\\d+");
  IElementType GT = new ArmaConfigTokenType(">");
  IElementType IDENT = new ArmaConfigTokenType("regex:[A-Za-z_][A-Za-z0-9_]*");
  IElementType LBRACE = new ArmaConfigTokenType("{");
  IElementType LBRACKET = new ArmaConfigTokenType("[");
  IElementType LINE_COMMENT = new ArmaConfigTokenType("regex://.*");
  IElementType LPAREN = new ArmaConfigTokenType("(");
  IElementType LT = new ArmaConfigTokenType("<");
  IElementType MAX_KEYWORD = new ArmaConfigTokenType("max");
  IElementType MINUS = new ArmaConfigTokenType("-");
  IElementType MIN_KEYWORD = new ArmaConfigTokenType("min");
  IElementType NUMBER = new ArmaConfigTokenType("regex:\\\\d+");
  IElementType PERCENT = new ArmaConfigTokenType("%");
  IElementType PLUS = new ArmaConfigTokenType("+");
  IElementType PREPROCESSOR = new ArmaConfigTokenType("regex:#.*");
  IElementType RBRACE = new ArmaConfigTokenType("}");
  IElementType RBRACKET = new ArmaConfigTokenType("]");
  IElementType RPAREN = new ArmaConfigTokenType(")");
  IElementType SEMICOLON = new ArmaConfigTokenType(";");
  IElementType SINGLE_QUOTE_BLOCK_TOKEN = new ArmaConfigTokenType("singleQuoteBlock");
  IElementType SLASH = new ArmaConfigTokenType("/");
  IElementType STAR = new ArmaConfigTokenType("*");
  IElementType STRING = new ArmaConfigTokenType("regex:\"([^\"\\\\\\\\n\\\\\\\\r]|\"\")*\"");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == ARRAY) {
        return new ArrayImpl(node);
      }
      else if (type == ARRAY_SUFFIX) {
        return new ArraySuffixImpl(node);
      }
      else if (type == ASSIGNMENT) {
        return new AssignmentImpl(node);
      }
      else if (type == CLASS_DECL) {
        return new ClassDeclImpl(node);
      }
      else if (type == CLASS_EXT) {
        return new ClassExtImpl(node);
      }
      else if (type == CLASS_FORWARD_DECL) {
        return new ClassForwardDeclImpl(node);
      }
      else if (type == CLASS_IDENT) {
        return new ClassIdentImpl(node);
      }
      else if (type == CLASS_NAME) {
        return new ClassNameImpl(node);
      }
      else if (type == DELETE_STMT) {
        return new DeleteStmtImpl(node);
      }
      else if (type == EXPR) {
        return new ExprImpl(node);
      }
      else if (type == EXPR_ADD) {
        return new ExprAddImpl(node);
      }
      else if (type == EXPR_MUL) {
        return new ExprMulImpl(node);
      }
      else if (type == EXPR_POWER) {
        return new ExprPowerImpl(node);
      }
      else if (type == EXPR_UNARY) {
        return new ExprUnaryImpl(node);
      }
      else if (type == MACRO_ATOM) {
        return new MacroAtomImpl(node);
      }
      else if (type == MACRO_INNER) {
        return new MacroInnerImpl(node);
      }
      else if (type == MACRO_INNER_TOKEN) {
        return new MacroInnerTokenImpl(node);
      }
      else if (type == MACRO_INVOCATION) {
        return new MacroInvocationImpl(node);
      }
      else if (type == MACRO_STMT) {
        return new MacroStmtImpl(node);
      }
      else if (type == PREPROC_VALUE) {
        return new PreprocValueImpl(node);
      }
      else if (type == PREPROC_VALUE_TOKEN) {
        return new PreprocValueTokenImpl(node);
      }
      else if (type == PRIMARY) {
        return new PrimaryImpl(node);
      }
      else if (type == SINGLE_QUOTE_BLOCK) {
        return new SingleQuoteBlockImpl(node);
      }
      else if (type == VALUE) {
        return new ValueImpl(node);
      }
      else if (type == VALUE_LIST) {
        return new ValueListImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
