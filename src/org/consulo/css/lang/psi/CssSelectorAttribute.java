package org.consulo.css.lang.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import org.consulo.css.lang.CssTokens;
import org.consulo.xstylesheet.psi.PsiXStyleSheetSelectorAttribute;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author VISTALL
 * @since 11.07.13.
 */
public class CssSelectorAttribute extends CssElement implements PsiXStyleSheetSelectorAttribute {
	public CssSelectorAttribute(@NotNull ASTNode node) {
		super(node);
	}

	@Override
	public PsiElement getValue() {
		return findChildByType(CssTokens.STRING);
	}

	@Nullable
	@Override
	public PsiElement getNameIdentifier() {
		return getFirstChild();
	}

	@Override
	public PsiElement setName(@NonNls @NotNull String s) throws IncorrectOperationException {
		return null;
	}
}
