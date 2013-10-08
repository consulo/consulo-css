package org.consulo.css.lang.psi;

import org.consulo.xstylesheet.psi.PsiXStyleSheetFunctionCallParameterList;
import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;

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
}
