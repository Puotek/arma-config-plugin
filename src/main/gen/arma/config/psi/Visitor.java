// This is a generated file. Not intended for manual editing.
package arma.config.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiElement;

public class Visitor extends PsiElementVisitor {

  public void visitAssignment(@NotNull Assignment o) {
    visitPsiElement(o);
  }

  public void visitClassDecl(@NotNull ClassDecl o) {
    visitPsiElement(o);
  }

  public void visitValue(@NotNull Value o) {
    visitPsiElement(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}
