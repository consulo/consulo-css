package org.consulo.xstylesheet.editor.lineMarker;

import com.intellij.openapi.editor.ElementColorProvider;
import com.intellij.psi.PsiElement;
import org.consulo.xstylesheet.definition.XStyleSheetProperty;
import org.consulo.xstylesheet.definition.value.impl.ColorXStyleSheetValue;
import org.consulo.xstylesheet.psi.PsiXStyleSheetProperty;
import org.consulo.xstylesheet.psi.PsiXStyleSheetPropertyValuePart;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class XStyleSheetElementColorProvider implements ElementColorProvider {
    @Nullable
    @Override
    public Color getColorFrom(@NotNull PsiElement psiElement) {
        if(psiElement instanceof PsiXStyleSheetPropertyValuePart) {
          Object value = ((PsiXStyleSheetPropertyValuePart) psiElement).getValue();
          if(value instanceof Color) {
            return (Color) value;
          }
        }
        return null;
    }

    @Override
    public void setColorTo(@NotNull PsiElement psiElement, @NotNull Color color) {
        //TODO [VISTALL] !
    }
}
