package org.consulo.css.lang.psi;

import org.consulo.css.lang.CssTokens;
import org.consulo.xstylesheet.psi.PsiXStyleSheetFunctionCallParameterList;
import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.TokenSet;

/**
 * @author VISTALL
 * @since 08.10.13.
 */
public class CssFunctionCallParameterList extends CssElement implements PsiXStyleSheetFunctionCallParameterList
{
	public CssFunctionCallParameterList(@NotNull ASTNode node)
	{
		super(node);
	}

	@Override
	public PsiElement[] getParameters()
	{
		return findChildrenByType(TokenSet.create(CssTokens.STRING, CssTokens.FUNCTION_ARGUMENT, CssTokens.NUMBER), PsiElement.class);
	}
}
