package org.consulo.css.lang.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import org.consulo.css.lang.CssTokens;
import org.consulo.xstylesheet.psi.PsiXStypeSheetUriLink;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 17.07.13.
 */
public class CssUriLink extends CssElement implements PsiXStypeSheetUriLink {
	public CssUriLink(@NotNull ASTNode node) {
		super(node);
	}

	@Override
	public String getUriText() {
		return null;
	}

	@Override
	public PsiElement getUri() {
		return findChildByType(CssTokens.URI_TEXT);
	}
}
