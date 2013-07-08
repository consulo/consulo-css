package org.consulo.xstylesheet.psi;

import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 08.07.13.
 */
public interface PsiXStyleSheetSelectorReferenceList extends PsiXStyleSheetElement {
  @NotNull
  PsiXStyleSheetSelectorReference[] getSelectorReferences();
}
