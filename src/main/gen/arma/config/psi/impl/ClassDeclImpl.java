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

public class ClassDeclImpl extends ASTWrapperPsiElement implements ClassDecl {

  public ClassDeclImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull Visitor visitor) {
    visitor.visitClassDecl(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof Visitor) accept((Visitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<Assignment> getAssignmentList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, Assignment.class);
  }

  @Override
  @NotNull
  public List<ClassDecl> getClassDeclList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ClassDecl.class);
  }

  @Override
  @Nullable
  public ClassExt getClassExt() {
    return findChildByClass(ClassExt.class);
  }

}
