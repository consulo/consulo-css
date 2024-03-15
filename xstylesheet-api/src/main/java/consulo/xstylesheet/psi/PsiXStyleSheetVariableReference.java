package consulo.xstylesheet.psi;

import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiNameIdentifierOwner;
import consulo.language.psi.PsiPolyVariantReference;

import java.util.function.Consumer;

/**
 * @author VISTALL
 * @since 2024-03-16
 */
public interface PsiXStyleSheetVariableReference extends PsiElement, PsiPolyVariantReference
{
	void visitVariants(Consumer<PsiNameIdentifierOwner> consumer);
}
