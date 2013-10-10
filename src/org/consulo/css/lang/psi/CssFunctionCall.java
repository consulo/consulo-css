package org.consulo.css.lang.psi;

import org.consulo.css.lang.CssTokens;
import org.consulo.xstylesheet.psi.PsiXStyleSheetFunctionCall;
import org.consulo.xstylesheet.psi.PsiXStyleSheetFunctionCallParameterList;
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
	public PsiElement getNameIdentifier()
	{
		return findNotNullChildByType(CssTokens.FUNCTION_NAME);
	}

	@Override
	public String getName()
	{
		PsiElement nameIdentifier = getNameIdentifier();

		return nameIdentifier.getText();
	}

	@Override
	public PsiXStyleSheetFunctionCallParameterList getParameterList()
	{
		return findChildByClass(PsiXStyleSheetFunctionCallParameterList.class);
	}

	@Override
	public PsiElement[] getParameters()
	{
		PsiXStyleSheetFunctionCallParameterList parameterList = getParameterList();
		if(parameterList == null)
		{
			return PsiElement.EMPTY_ARRAY;
		}
		return parameterList.getParameters();
	}
}
