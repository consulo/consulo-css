package org.consulo.css.lang.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import org.consulo.css.lang.CssTokens;
import org.consulo.xstylesheet.psi.PsiXStyleSheetSelectorPseudoClass;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author VISTALL
 * @since 11.07.13.
 */
public class CssSelectorPseudoClass extends CssElement implements PsiXStyleSheetSelectorPseudoClass {
	public CssSelectorPseudoClass(@NotNull ASTNode node) {
		super(node);
	}

	@Nullable
	@Override
	public PsiElement getNameIdentifier() {
		return findChildByType(CssTokens.IDENTIFIER);
	}

	@Override
	public String getName() {
		PsiElement nameIdentifier = getNameIdentifier();
		return nameIdentifier == null ? null : nameIdentifier.getText();
	}

	@Override
	public PsiElement setName(@NonNls @NotNull String s) throws IncorrectOperationException {
		return null;
	}
}
