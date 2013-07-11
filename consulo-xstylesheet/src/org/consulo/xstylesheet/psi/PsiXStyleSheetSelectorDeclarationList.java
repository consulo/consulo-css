package org.consulo.xstylesheet.psi;

import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 08.07.13.
 */
public interface PsiXStyleSheetSelectorDeclarationList extends PsiXStyleSheetElement {
  @NotNull
  PsiXStyleSheetSelectorDeclaration[] getSelectorDeclarations();
}
