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

public class ClassBodyImpl extends ASTWrapperPsiElement implements ClassBody {

  public ClassBodyImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull Visitor visitor) {
    visitor.visitClassBody(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof Visitor) accept((Visitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<ArrayBlock> getArrayBlockList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ArrayBlock.class);
  }

  @Override
  @NotNull
  public List<ClassBlock> getClassBlockList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ClassBlock.class);
  }

  @Override
  @NotNull
  public List<DeleteBlock> getDeleteBlockList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DeleteBlock.class);
  }

  @Override
  @NotNull
  public List<Identifier> getIdentifierList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, Identifier.class);
  }

  @Override
  @NotNull
  public List<ParameterBlock> getParameterBlockList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ParameterBlock.class);
  }

}
