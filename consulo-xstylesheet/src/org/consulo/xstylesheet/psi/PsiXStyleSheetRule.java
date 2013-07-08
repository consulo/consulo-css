package org.consulo.xstylesheet.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author VISTALL
 * @since 08.07.13.
 */
public interface PsiXStyleSheetRule extends PsiElement {
  @Nullable
  PsiXStyleSheetBlock getBlock();

  @NotNull
  PsiXStyleSheetProperty[] getProperties();

  PsiElement getOnlyNameIdentifier();

  boolean isIdRule();

  boolean isClassRule();
}
