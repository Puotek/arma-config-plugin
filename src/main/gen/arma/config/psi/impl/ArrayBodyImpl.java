// This is a generated file. Not intended for manual editing.
package arma.config.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static arma.config.psi.CfgTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import arma.config.psi.*;

public class ArrayBodyImpl extends ASTWrapperPsiElement implements ArrayBody {

  public ArrayBodyImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull Visitor visitor) {
    visitor.visitArrayBody(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof Visitor) accept((Visitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<ArrayBody> getArrayBodyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ArrayBody.class);
  }

  @Override
  @NotNull
  public List<ParameterValue> getParameterValueList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ParameterValue.class);
  }

}
