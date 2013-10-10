package org.consulo.xstylesheet.editor.lineMarker;

import java.awt.Color;

import org.consulo.xstylesheet.psi.PsiXStyleSheetPropertyValuePart;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.editor.ElementColorProvider;
import com.intellij.psi.PsiElement;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class XStyleSheetElementColorProvider implements ElementColorProvider {
    @Nullable
    @Override
    public Color getColorFrom(@NotNull PsiElement psiElement) {
        if(psiElement instanceof PsiXStyleSheetPropertyValuePart) {
			Object value = null;
			try
			{
				value = ((PsiXStyleSheetPropertyValuePart) psiElement).getValue();
			}
			catch(Exception e)
			{
				System.out.println(psiElement.getText());
				e.printStackTrace();
			}
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
