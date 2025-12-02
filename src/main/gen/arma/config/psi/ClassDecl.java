// This is a generated file. Not intended for manual editing.
package arma.config.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface ClassDecl extends PsiElement {

  @NotNull
  List<Assignment> getAssignmentList();

  @NotNull
  List<ClassDecl> getClassDeclList();

  @Nullable
  ClassExt getClassExt();

  @NotNull
  List<ClassForwardDecl> getClassForwardDeclList();

  @NotNull
  List<DeleteStmt> getDeleteStmtList();

  @NotNull
  List<MacroStmt> getMacroStmtList();

}
