package org.consulo.xstylesheet.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;

/**
 * @author VISTALL
 * @since 11.07.13.
 */
public interface PsiXStyleSheetSelectorAttribute extends PsiXStyleSheetElement, PsiNameIdentifierOwner {
	PsiElement getValue();
}
