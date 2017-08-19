package consulo.css.lang.psi;

import org.jetbrains.annotations.NotNull;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import consulo.xstylesheet.psi.XStyleSheetRoot;

/**
 * @author VISTALL
 * @since 19-Aug-17
 */
public class CssRoot extends ASTWrapperPsiElement implements XStyleSheetRoot
{
	public CssRoot(@NotNull ASTNode node)
	{
		super(node);
	}
}
