package org.consulo.xstylesheet.psi;

import com.intellij.psi.PsiElement;

/**
 * @author VISTALL
 * @since 08.07.13.
 */
public interface PsiXStyleSheetRule extends PsiElement {
  PsiElement getOnlyNameIdentifier();

  boolean isIdRule();

  boolean isClassRule();
}
