package org.consulo.xstylesheet.psi;

import com.intellij.psi.PsiElement;

/**
 * @author VISTALL
 * @since 08.07.13.
 */
public interface PsiXStyleSheetBlock extends PsiElement {
  PsiXStyleSheetProperty[] getProperties();
}
