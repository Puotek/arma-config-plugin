// This is a generated file. Not intended for manual editing.
package arma.config.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import arma.config.psi.impl.*;

public interface ArmaConfigTypes {

  IElementType ARRAY_BLOCK = new ArmaConfigElementType("ARRAY_BLOCK");
  IElementType CLASS_BLOCK = new ArmaConfigElementType("CLASS_BLOCK");
  IElementType CLASS_BODY = new ArmaConfigElementType("CLASS_BODY");
  IElementType CLASS_EXTENSION = new ArmaConfigElementType("CLASS_EXTENSION");
  IElementType DELETE_BLOCK = new ArmaConfigElementType("DELETE_BLOCK");
  IElementType IDENTIFIER = new ArmaConfigElementType("IDENTIFIER");
  IElementType MATH_BLOCK = new ArmaConfigElementType("MATH_BLOCK");
  IElementType MATH_ELEMENT = new ArmaConfigElementType("MATH_ELEMENT");
  IElementType MATH_OPERATOR = new ArmaConfigElementType("MATH_OPERATOR");
  IElementType PARAMETER_BLOCK = new ArmaConfigElementType("PARAMETER_BLOCK");
  IElementType PARAMETER_VALUE = new ArmaConfigElementType("PARAMETER_VALUE");

  IElementType BLOCK_COMMENT = new ArmaConfigTokenType("BLOCK_COMMENT");
  IElementType CARET = new ArmaConfigTokenType("^");
  IElementType CLASS_KEYWORD = new ArmaConfigTokenType("class");
  IElementType COLON = new ArmaConfigTokenType(":");
  IElementType COMMA = new ArmaConfigTokenType(",");
  IElementType DELETE_KEYWORD = new ArmaConfigTokenType("delete");
  IElementType DOUBLE_HASH = new ArmaConfigTokenType("##");
  IElementType EQUAL = new ArmaConfigTokenType("=");
  IElementType EXCL = new ArmaConfigTokenType("!");
  IElementType FLOAT = new ArmaConfigTokenType("FLOAT");
  IElementType GT = new ArmaConfigTokenType(">");
  IElementType LBRACE = new ArmaConfigTokenType("{");
  IElementType LBRACKET = new ArmaConfigTokenType("[");
  IElementType LINE_COMMENT = new ArmaConfigTokenType("LINE_COMMENT");
  IElementType LPAREN = new ArmaConfigTokenType("(");
  IElementType LT = new ArmaConfigTokenType("<");
  IElementType MAX_KEYWORD = new ArmaConfigTokenType("max");
  IElementType MINUS = new ArmaConfigTokenType("-");
  IElementType MIN_KEYWORD = new ArmaConfigTokenType("min");
  IElementType NUMBER = new ArmaConfigTokenType("NUMBER");
  IElementType PERCENT = new ArmaConfigTokenType("%");
  IElementType PLUS = new ArmaConfigTokenType("+");
  IElementType PREPROCESSOR = new ArmaConfigTokenType("PREPROCESSOR");
  IElementType RBRACE = new ArmaConfigTokenType("}");
  IElementType RBRACKET = new ArmaConfigTokenType("]");
  IElementType RPAREN = new ArmaConfigTokenType(")");
  IElementType SEMICOLON = new ArmaConfigTokenType(";");
  IElementType SINGLE_HASH = new ArmaConfigTokenType("#");
  IElementType SINGLE_QUOTE = new ArmaConfigTokenType("SINGLE_QUOTE");
  IElementType SLASH = new ArmaConfigTokenType("/");
  IElementType STAR = new ArmaConfigTokenType("*");
  IElementType STRING = new ArmaConfigTokenType("STRING");
  IElementType TEXT = new ArmaConfigTokenType("TEXT");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == ARRAY_BLOCK) {
        return new ArrayBlockImpl(node);
      }
      else if (type == CLASS_BLOCK) {
        return new ClassBlockImpl(node);
      }
      else if (type == CLASS_BODY) {
        return new ClassBodyImpl(node);
      }
      else if (type == CLASS_EXTENSION) {
        return new ClassExtensionImpl(node);
      }
      else if (type == DELETE_BLOCK) {
        return new DeleteBlockImpl(node);
      }
      else if (type == IDENTIFIER) {
        return new IdentifierImpl(node);
      }
      else if (type == MATH_BLOCK) {
        return new MathBlockImpl(node);
      }
      else if (type == MATH_ELEMENT) {
        return new MathElementImpl(node);
      }
      else if (type == MATH_OPERATOR) {
        return new MathOperatorImpl(node);
      }
      else if (type == PARAMETER_BLOCK) {
        return new ParameterBlockImpl(node);
      }
      else if (type == PARAMETER_VALUE) {
        return new ParameterValueImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
