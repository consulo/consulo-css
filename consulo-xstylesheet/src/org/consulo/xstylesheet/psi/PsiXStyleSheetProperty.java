package org.consulo.xstylesheet.psi;

import com.intellij.psi.PsiElement;
import org.consulo.xstylesheet.definition.XStyleSheetProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public interface PsiXStyleSheetProperty extends PsiElement {
    @Nullable
    XStyleSheetProperty getXStyleSheetProperty();

    @NotNull
    Object[] getXStyleSheetValues();

    @Nullable
    String getName();
}
