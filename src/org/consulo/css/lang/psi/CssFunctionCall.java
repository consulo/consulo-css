package org.consulo.css.lang.psi;

import org.consulo.xstylesheet.psi.PsiXStyleSheetFunctionCall;
import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;

/**
 * @author VISTALL
 * @since 08.10.13.
 */
public class CssFunctionCall extends CssElement implements PsiXStyleSheetFunctionCall
{
	public CssFunctionCall(@NotNull ASTNode node)
	{
		super(node);
	}

	@Override
	public String getUriText()
	{
		return null;
	}

	@Override
	public PsiElement getUri()
	{
		return null;
	}
}
