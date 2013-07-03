package org.consulo.xstylesheet.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import org.consulo.xstylesheet.definition.XStyleSheetProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public interface PsiXStyleSheetProperty extends PsiElement, PsiNameIdentifierOwner {
  @Nullable
  XStyleSheetProperty getXStyleSheetProperty();

  @NotNull
  PsiXStyleSheetPropertyValuePart[] getParts();

  @Nullable
  String getName();
}
