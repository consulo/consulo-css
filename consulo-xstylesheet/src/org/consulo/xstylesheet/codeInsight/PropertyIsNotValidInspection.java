package org.consulo.xstylesheet.codeInsight;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.consulo.xstylesheet.definition.XStyleSheetProperty;
import org.consulo.xstylesheet.psi.PsiXStyleSheetProperty;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class PropertyIsNotValidInspection extends LocalInspectionTool {
  @NotNull
  @Override
  public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
    return new PsiElementVisitor() {
      @Override
      public void visitElement(PsiElement element) {
        if(element instanceof PsiXStyleSheetProperty) {
          XStyleSheetProperty xStyleSheetProperty = ((PsiXStyleSheetProperty) element).getXStyleSheetProperty();
          if(xStyleSheetProperty == null) {
            PsiElement nameIdentifier = ((PsiXStyleSheetProperty) element).getNameIdentifier();
            if(nameIdentifier == null) {
              return;
            }
            holder.registerProblem(nameIdentifier, "Invalid property name", ProblemHighlightType.WEAK_WARNING);
          }
        }
      }
    };
  }
}
