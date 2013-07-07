package org.consulo.xstylesheet.codeInsight;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.consulo.xstylesheet.definition.XStyleSheetProperty;
import org.consulo.xstylesheet.psi.PsiXStyleSheetProperty;
import org.consulo.xstylesheet.psi.PsiXStyleSheetPropertyValuePart;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 08.07.13.
 */
public class PropertyValueIsNotValidInspection extends LocalInspectionTool {
  @NotNull
  @Override
  public com.intellij.psi.PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
    return new PsiElementVisitor() {
      @Override
      public void visitElement(PsiElement element) {
        if(element instanceof PsiXStyleSheetProperty) {
          XStyleSheetProperty xStyleSheetProperty = ((PsiXStyleSheetProperty) element).getXStyleSheetProperty();
          if(xStyleSheetProperty == null) {
            return;
          }

          for (PsiXStyleSheetPropertyValuePart xStyleSheetPropertyValuePart : ((PsiXStyleSheetProperty) element).getParts()) {
            Object value = xStyleSheetPropertyValuePart.getValue();
            if(value == null) {
              holder.registerProblem(xStyleSheetPropertyValuePart, "Invalid property value", ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
            }
          }
        }
      }
    };
  }
}
