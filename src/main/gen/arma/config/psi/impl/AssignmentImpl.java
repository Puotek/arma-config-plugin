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

public class AssignmentImpl extends ASTWrapperPsiElement implements Assignment {

  public AssignmentImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull Visitor visitor) {
    visitor.visitAssignment(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof Visitor) accept((Visitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public ArraySuffix getArraySuffix() {
    return findChildByClass(ArraySuffix.class);
  }

  @Override
  @Nullable
  public MacroInvocation getMacroInvocation() {
    return findChildByClass(MacroInvocation.class);
  }

  @Override
  @NotNull
  public Value getValue() {
    return findNotNullChildByClass(Value.class);
  }

}
