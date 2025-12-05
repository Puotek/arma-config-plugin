// This is a generated file. Not intended for manual editing.
package arma.config.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface ClassBody extends PsiElement {

  @NotNull
  List<ArrayBlock> getArrayBlockList();

  @NotNull
  List<ClassBlock> getClassBlockList();

  @NotNull
  List<DeleteBlock> getDeleteBlockList();

  @NotNull
  List<Identifier> getIdentifierList();

  @NotNull
  List<ParameterBlock> getParameterBlockList();

}
