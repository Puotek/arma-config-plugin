// This is a generated file. Not intended for manual editing.
package arma.config.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static arma.config.psi.ArmaConfigTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import arma.config.psi.*;

public class MathOperatorImpl extends ASTWrapperPsiElement implements MathOperator {

  public MathOperatorImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull Visitor visitor) {
    visitor.visitMathOperator(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof Visitor) accept((Visitor)visitor);
    else super.accept(visitor);
  }

}
