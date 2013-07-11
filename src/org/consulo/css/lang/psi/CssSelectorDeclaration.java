package org.consulo.css.lang.psi;

import com.intellij.lang.ASTNode;
import org.consulo.xstylesheet.psi.PsiXStyleSheetSelectorDeclaration;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 11.07.13.
 */
public class CssSelectorDeclaration extends CssElement implements PsiXStyleSheetSelectorDeclaration {
	public static final CssSelectorDeclaration[] EMPTY_ARRAY = new CssSelectorDeclaration[0];

	public CssSelectorDeclaration(@NotNull ASTNode node) {
		super(node);
	}

	@Override
	public CssSelectorReference[] getSelectorReferences() {
		return findChildrenByClass(CssSelectorReference.class);
	}
}
