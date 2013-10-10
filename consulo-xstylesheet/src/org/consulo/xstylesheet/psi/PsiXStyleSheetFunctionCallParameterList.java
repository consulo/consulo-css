package org.consulo.xstylesheet.psi;

import com.intellij.psi.PsiElement;

/**
 * @author VISTALL
 * @since 08.10.13.
 */
public interface PsiXStyleSheetFunctionCallParameterList extends PsiXStyleSheetElement
{
	PsiElement[] getParameters();
}
