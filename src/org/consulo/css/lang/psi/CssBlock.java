package org.consulo.css.lang.psi;

import com.intellij.lang.ASTNode;
import org.consulo.xstylesheet.psi.PsiXStyleSheetBlock;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class CssBlock extends CssElement implements PsiXStyleSheetBlock {
  public CssBlock(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public CssProperty[] getProperties() {
    return findChildrenByClass(CssProperty.class);
  }
}
