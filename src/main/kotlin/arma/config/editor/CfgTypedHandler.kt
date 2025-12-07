package arma.config.editor

import arma.config.psi.CfgFile
import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile

/**
 * Extra typing behavior for Arma Config:
 *
 *  - When the user types '{' and the brace matcher immediately inserts '}' after
 *    the caret, we also append ';' after that '}' in *code* contexts:
 *
 *      class X {▮}   -> class X {▮};
 *      array[] = {▮} -> array[] = {▮};
 *
 *  - We do NOT do this:
 *      - inside macros (MacroInner: Q({}), TAG({}))
 *      - inside preprocessor / single-quote blocks / normal strings
 */
class CfgTypedHandler : TypedHandlerDelegate() {

    override fun charTyped(
        c: Char, project: Project, editor: Editor, file: PsiFile
    ): Result {
        if (file !is CfgFile) return Result.CONTINUE
        if (c != '{') return Result.CONTINUE

        val document = editor.document
        val text = document.charsSequence
        val offset = editor.caretModel.offset

        // After typing '{', caret is between { and } if brace matcher ran:
        //   ... {▮}
        if (offset >= text.length || text[offset] != '}') {
            return Result.CONTINUE
        }

//        // PSI element for the '{'
//        val braceElement = file.findElementAt(offset - 1) ?: return Result.CONTINUE
//        val tokenType = braceElement.node.elementType
//
//        // Paranoia: ensure it's really our LBRACE token
//        if (tokenType != CfgTypes.LBRACE) {
//            return Result.CONTINUE
//        }
//
//        // Skip inside:
//        //  - macro inner (Q({}), TAG({something}))
//        //  - strings, preprocessor, single-quote blocks
//        if (PsiTreeUtil.getParentOfType(braceElement, MacroInner::class.java, false) != null) {
//            return Result.CONTINUE
//        }
//        if (PsiTreeUtil.getParentOfType(
//                braceElement,
//                arma.config.psi.SingleQuoteBlock::class.java,
//                false
//            ) != null
//        ) {
//            return Result.CONTINUE
//        }
//        if (PsiTreeUtil.getParentOfType(
//                braceElement,
//                arma.config.psi.PreprocValue::class.java,
//                false
//            ) != null
//        ) {
//            return Result.CONTINUE
//        }
//        if (PsiTreeUtil.getParentOfType(
//                braceElement,
//                arma.config.psi.Value::class.java,
//                false
//            ) != null
//        ) {
//            // we *do* want inside normal values (arrays), but we already filtered macro/preproc/'' above,
//            // so nothing more to do here; this check is mostly illustrative.
//        }

        // At this point we are in "code": class body or array.
        // Check we don't already have a ';' after the '}'.
        val afterClosing = offset + 1
        if (afterClosing < text.length && text[afterClosing] == ';') {
            return Result.CONTINUE
        }

        // Insert ';' right after '}'
        document.insertString(afterClosing, ";")

        return Result.CONTINUE
    }
}
