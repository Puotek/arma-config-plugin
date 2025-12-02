// This is a generated file. Not intended for manual editing.
package arma.config.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import arma.config.psi.impl.*;

public interface ArmaConfigTypes {

  IElementType ASSIGNMENT = new IElementType("ASSIGNMENT", null);
  IElementType CLASS_DECL = new IElementType("CLASS_DECL", null);
  IElementType VALUE = new IElementType("VALUE", null);

  IElementType IDENT = new ArmaConfigTokenType("IDENT");
  IElementType NUMBER = new ArmaConfigTokenType("NUMBER");
  IElementType STRING = new ArmaConfigTokenType("STRING");
  IElementType TOKENS = new ArmaConfigTokenType("tokens");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == ASSIGNMENT) {
        return new AssignmentImpl(node);
      }
      else if (type == CLASS_DECL) {
        return new ClassDeclImpl(node);
      }
      else if (type == VALUE) {
        return new ValueImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
