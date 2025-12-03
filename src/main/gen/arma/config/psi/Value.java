// This is a generated file. Not intended for manual editing.
package arma.config.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface Value extends PsiElement {

  @Nullable
  Array getArray();

  @Nullable
  Expr getExpr();

  @Nullable
  MacroInvocation getMacroInvocation();

  @Nullable
  PreprocValue getPreprocValue();

  @Nullable
  SingleQuoteBlock getSingleQuoteBlock();

}
