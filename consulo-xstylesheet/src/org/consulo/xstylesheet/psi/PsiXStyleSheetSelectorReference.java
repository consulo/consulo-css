package org.consulo.xstylesheet.psi;

import com.intellij.psi.PsiNameIdentifierOwner;

/**
 * @author VISTALL
 * @since 08.07.13.
 */
public interface PsiXStyleSheetSelectorReference extends PsiXStyleSheetElement, PsiNameIdentifierOwner {

  boolean isClassRule();

  boolean isIdRule();
}
