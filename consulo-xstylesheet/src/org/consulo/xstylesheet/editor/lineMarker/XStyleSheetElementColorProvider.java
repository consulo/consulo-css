package org.consulo.xstylesheet.editor.lineMarker;

import com.intellij.openapi.editor.ElementColorProvider;
import com.intellij.psi.PsiElement;
import org.consulo.xstylesheet.definition.XStyleSheetProperty;
import org.consulo.xstylesheet.definition.value.impl.ColorXStyleSheetValue;
import org.consulo.xstylesheet.psi.PsiXStyleSheetProperty;
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
        if(psiElement instanceof PsiXStyleSheetProperty) {
            XStyleSheetProperty xStyleSheetProperty = ((PsiXStyleSheetProperty) psiElement).getXStyleSheetProperty();
            if(xStyleSheetProperty instanceof ColorXStyleSheetValue) {
                Object[] xStyleSheetValues = ((PsiXStyleSheetProperty) psiElement).getXStyleSheetValues();
                return (Color) xStyleSheetValues[0];
            }
        }
        return null;
    }

    @Override
    public void setColorTo(@NotNull PsiElement psiElement, @NotNull Color color) {
        //TODO [VISTALL] !
    }
}
