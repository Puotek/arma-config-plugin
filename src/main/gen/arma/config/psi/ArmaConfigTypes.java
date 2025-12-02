// This is a generated file. Not intended for manual editing.
package arma.config.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import arma.config.psi.impl.*;

public interface ArmaConfigTypes {

  IElementType ARRAY = new IElementType("ARRAY", null);
  IElementType ARRAY_SUFFIX = new IElementType("ARRAY_SUFFIX", null);
  IElementType ASSIGNMENT = new IElementType("ASSIGNMENT", null);
  IElementType CLASS_DECL = new IElementType("CLASS_DECL", null);
  IElementType CLASS_EXT = new IElementType("CLASS_EXT", null);
  IElementType VALUE = new IElementType("VALUE", null);
  IElementType VALUE_LIST = new IElementType("VALUE_LIST", null);

  IElementType BLOCK_COMMENT = new ArmaConfigTokenType("regex:/\\*([^*]|\\*+[^*/])*\\*+/");
  IElementType CLASS_KEYWORD = new ArmaConfigTokenType("class");
  IElementType COLON = new ArmaConfigTokenType(":");
  IElementType COMMA = new ArmaConfigTokenType(",");
  IElementType EQUAL = new ArmaConfigTokenType("=");
  IElementType IDENT = new ArmaConfigTokenType("regex:[A-Za-z_][A-Za-z0-9_]*");
  IElementType LBRACE = new ArmaConfigTokenType("{");
  IElementType LBRACKET = new ArmaConfigTokenType("[");
  IElementType LINE_COMMENT = new ArmaConfigTokenType("regex://.*");
  IElementType NUMBER = new ArmaConfigTokenType("regex:\\d+");
  IElementType PREPROCESSOR = new ArmaConfigTokenType("regex:#.*");
  IElementType RBRACE = new ArmaConfigTokenType("}");
  IElementType RBRACKET = new ArmaConfigTokenType("]");
  IElementType SEMICOLON = new ArmaConfigTokenType(";");
  IElementType STRING = new ArmaConfigTokenType("regex:\"([^\"\\\\n]|\\\\.)*\"");

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
