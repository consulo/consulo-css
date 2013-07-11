package org.consulo.css.lang.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.util.IncorrectOperationException;
import org.consulo.css.lang.CssTokens;
import org.consulo.xstylesheet.psi.PsiXStyleSheetSelectorReference;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class CssSelectorReference extends CssElement implements PsiXStyleSheetSelectorReference, PsiNameIdentifierOwner {
	public static final CssSelectorReference[] EMPTY_ARRAY = new CssSelectorReference[0];

	public CssSelectorReference(@NotNull ASTNode node) {
		super(node);
	}

	@Override
	public boolean isClassRule() {
		return getFirstChild().getNode().getElementType() == CssTokens.DOT;
	}

	@Override
	public boolean isIdRule() {
		return getFirstChild().getNode().getElementType() == CssTokens.SHARP;
	}

	@Nullable
	@Override
	public PsiElement getNameIdentifier() {
		return findChildByType(CssTokens.IDENTIFIER);
	}

	@Override
	public String getName() {
		PsiElement nameIdentifier = getNameIdentifier();
		return nameIdentifier == null ? getText() : nameIdentifier.getText();
	}

	@Override
	public PsiElement setName(@NonNls @NotNull String s) throws IncorrectOperationException {
		return null;
	}
}
