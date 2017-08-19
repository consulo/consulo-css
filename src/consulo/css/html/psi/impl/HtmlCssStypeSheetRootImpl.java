package consulo.css.html.psi.impl;

import org.jetbrains.annotations.NotNull;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import consulo.xstylesheet.psi.XStyleSheetRoot;

/**
 * @author VISTALL
 * @since 19-Aug-17
 */
public class HtmlCssStypeSheetRootImpl extends ASTWrapperPsiElement implements XStyleSheetRoot
{
	public HtmlCssStypeSheetRootImpl(@NotNull ASTNode node)
	{
		super(node);
	}
}
