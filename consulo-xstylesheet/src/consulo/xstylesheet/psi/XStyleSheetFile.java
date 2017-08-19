package consulo.xstylesheet.psi;

import org.jetbrains.annotations.NotNull;
import com.intellij.psi.PsiFile;

/**
 * @author VISTALL
 * @since 05.12.2015
 */
public interface XStyleSheetFile extends PsiFile
{
	@NotNull
	XStyleSheetRoot getRoot();
}
