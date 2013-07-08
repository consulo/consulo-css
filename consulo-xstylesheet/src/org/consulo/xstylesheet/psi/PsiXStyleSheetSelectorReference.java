package org.consulo.xstylesheet.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 08.07.13.
 */
public interface PsiXStyleSheetSelectorReference extends PsiElement {

  @NotNull
  String getDisplayName();

  boolean isClassRule();

  boolean isIdRule();
}
