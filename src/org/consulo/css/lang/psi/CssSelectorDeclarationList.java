package org.consulo.css.lang.psi;

import com.intellij.lang.ASTNode;
import org.consulo.xstylesheet.psi.PsiXStyleSheetSelectorDeclarationList;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 08.07.13.
 */
public class CssSelectorDeclarationList extends CssElement implements PsiXStyleSheetSelectorDeclarationList {
  public CssSelectorDeclarationList(@NotNull ASTNode node) {
    super(node);
  }

  @NotNull
  @Override
  public CssSelectorDeclaration[] getSelectorDeclarations() {
    return findChildrenByClass(CssSelectorDeclaration.class);
  }
}
