package org.consulo.xstylesheet.psi;

import com.intellij.psi.PsiElement;

/**
 * @author VISTALL
 * @since 17.07.13.
 */
public interface PsiXStypeSheetUriLink extends PsiXStyleSheetElement{
	String getUriText();

	PsiElement getUri();
}
