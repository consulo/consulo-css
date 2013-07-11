package org.consulo.xstylesheet.psi;

/**
 * @author VISTALL
 * @since 11.07.13.
 */
public interface PsiXStyleSheetSelectorDeclaration extends PsiXStyleSheetElement {
	PsiXStyleSheetSelectorReference[] getSelectorReferences();
}
