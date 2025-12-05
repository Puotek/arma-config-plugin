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

public class MathBlockImpl extends ASTWrapperPsiElement implements MathBlock {

  public MathBlockImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull Visitor visitor) {
    visitor.visitMathBlock(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof Visitor) accept((Visitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<MathElement> getMathElementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, MathElement.class);
  }

  @Override
  @NotNull
  public List<MathOperator> getMathOperatorList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, MathOperator.class);
  }

}
