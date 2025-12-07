// This is a generated file. Not intended for manual editing.
package arma.config.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import arma.config.psi.impl.*;

public interface CfgTypes {

  IElementType ARRAY_BLOCK = new CfgElementType("ARRAY_BLOCK");
  IElementType ARRAY_BODY = new CfgElementType("ARRAY_BODY");
  IElementType CLASS_BLOCK = new CfgElementType("CLASS_BLOCK");
  IElementType CLASS_BODY = new CfgElementType("CLASS_BODY");
  IElementType CLASS_EXTENSION = new CfgElementType("CLASS_EXTENSION");
  IElementType DELETE_BLOCK = new CfgElementType("DELETE_BLOCK");
  IElementType IDENTIFIER = new CfgElementType("IDENTIFIER");
  IElementType MATH_BLOCK = new CfgElementType("MATH_BLOCK");
  IElementType MATH_ELEMENT = new CfgElementType("MATH_ELEMENT");
  IElementType MATH_OPERATOR = new CfgElementType("MATH_OPERATOR");
  IElementType PARAMETER_BLOCK = new CfgElementType("PARAMETER_BLOCK");
  IElementType PARAMETER_VALUE = new CfgElementType("PARAMETER_VALUE");

  IElementType AMPERSAND = new CfgTokenType("&");
  IElementType BLOCK_COMMENT = new CfgTokenType("BLOCK_COMMENT");
  IElementType CARET = new CfgTokenType("^");
  IElementType CLASS_KEYWORD = new CfgTokenType("class");
  IElementType COLON = new CfgTokenType(":");
  IElementType COMMA = new CfgTokenType(",");
  IElementType DELETE_KEYWORD = new CfgTokenType("delete");
  IElementType DOUBLE_HASH = new CfgTokenType("##");
  IElementType EQUAL = new CfgTokenType("=");
  IElementType EXCL = new CfgTokenType("!");
  IElementType FLOAT = new CfgTokenType("FLOAT");
  IElementType GT = new CfgTokenType(">");
  IElementType LBRACE = new CfgTokenType("{");
  IElementType LBRACKET = new CfgTokenType("[");
  IElementType LINE_COMMENT = new CfgTokenType("LINE_COMMENT");
  IElementType LPAREN = new CfgTokenType("(");
  IElementType LT = new CfgTokenType("<");
  IElementType MAX_KEYWORD = new CfgTokenType("max");
  IElementType MINUS = new CfgTokenType("-");
  IElementType MIN_KEYWORD = new CfgTokenType("min");
  IElementType NUMBER = new CfgTokenType("NUMBER");
  IElementType PERCENT = new CfgTokenType("%");
  IElementType PLUS = new CfgTokenType("+");
  IElementType PREPROCESSOR = new CfgTokenType("PREPROCESSOR");
  IElementType RBRACE = new CfgTokenType("}");
  IElementType RBRACKET = new CfgTokenType("]");
  IElementType RPAREN = new CfgTokenType(")");
  IElementType SEMICOLON = new CfgTokenType(";");
  IElementType SINGLE_HASH = new CfgTokenType("#");
  IElementType SINGLE_QUOTE = new CfgTokenType("SINGLE_QUOTE");
  IElementType SLASH = new CfgTokenType("/");
  IElementType STAR = new CfgTokenType("*");
  IElementType STRING = new CfgTokenType("STRING");
  IElementType TEXT = new CfgTokenType("TEXT");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == ARRAY_BLOCK) {
        return new ArrayBlockImpl(node);
      }
      else if (type == ARRAY_BODY) {
        return new ArrayBodyImpl(node);
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
