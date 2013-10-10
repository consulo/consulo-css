package org.consulo.xstylesheet.psi;

import com.intellij.psi.PsiElement;

/**
 * @author VISTALL
 * @since 17.07.13.
 */
public interface PsiXStyleSheetFunctionCall extends PsiXStyleSheetElement{
	PsiElement getNameIdentifier();

	String getName();

	PsiXStyleSheetFunctionCallParameterList getParameterList();

	PsiElement[] getParameters();
}
