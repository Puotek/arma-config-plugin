// This is a generated file. Not intended for manual editing.
package arma.config.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiElement;

public class Visitor extends PsiElementVisitor {

  public void visitArray(@NotNull Array o) {
    visitPsiElement(o);
  }

  public void visitArraySuffix(@NotNull ArraySuffix o) {
    visitPsiElement(o);
  }

  public void visitAssignment(@NotNull Assignment o) {
    visitPsiElement(o);
  }

  public void visitClassDecl(@NotNull ClassDecl o) {
    visitPsiElement(o);
  }

  public void visitClassExt(@NotNull ClassExt o) {
    visitPsiElement(o);
  }

  public void visitClassForwardDecl(@NotNull ClassForwardDecl o) {
    visitPsiElement(o);
  }

  public void visitDeleteStmt(@NotNull DeleteStmt o) {
    visitPsiElement(o);
  }

  public void visitValue(@NotNull Value o) {
    visitPsiElement(o);
  }

  public void visitValueList(@NotNull ValueList o) {
    visitPsiElement(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}
