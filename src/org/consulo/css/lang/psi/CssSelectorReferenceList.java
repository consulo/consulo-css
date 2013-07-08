package org.consulo.css.lang.psi;

import com.intellij.lang.ASTNode;
import org.consulo.xstylesheet.psi.PsiXStyleSheetSelectorReferenceList;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 08.07.13.
 */
public class CssSelectorReferenceList extends CssElement implements PsiXStyleSheetSelectorReferenceList {
  public CssSelectorReferenceList(@NotNull ASTNode node) {
    super(node);
  }

  @NotNull
  @Override
  public CssSelectorReference[] getSelectorReferences() {
    return findChildrenByClass(CssSelectorReference.class);
  }
}
