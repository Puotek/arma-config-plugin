// This is a generated file. Not intended for manual editing.
package arma.config.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;

public interface ClassDecl extends PsiNamedElement {

  @NotNull
  List<Assignment> getAssignmentList();

  @NotNull
  List<ClassDecl> getClassDeclList();

}
