package consulo.css.lang.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import consulo.annotation.access.RequiredReadAction;
import consulo.xstylesheet.psi.PsiXStyleSheetRule;
import consulo.xstylesheet.psi.XStyleSheetRoot;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 19-Aug-17
 */
public class CssRoot extends ASTWrapperPsiElement implements XStyleSheetRoot
{
	public CssRoot(@Nonnull ASTNode node)
	{
		super(node);
	}

	@RequiredReadAction
	@Nonnull
	@Override
	public PsiXStyleSheetRule[] getRules()
	{
		return findChildrenByClass(PsiXStyleSheetRule.class);
	}
}
