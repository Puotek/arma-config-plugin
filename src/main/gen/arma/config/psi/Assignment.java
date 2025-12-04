// This is a generated file. Not intended for manual editing.
package arma.config.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface Assignment extends PsiElement {

  @Nullable
  ArraySuffix getArraySuffix();

  @Nullable
  MacroInvocation getMacroInvocation();

  @NotNull
  Value getValue();

}
