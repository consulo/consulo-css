package org.consulo.css.lang.psi;

import com.intellij.lang.ASTNode;
import org.consulo.xstylesheet.psi.PsiXStyleSheetRule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class CssRule extends CssElement implements PsiXStyleSheetRule {
  public CssRule(@NotNull ASTNode node) {
    super(node);
  }

  @Nullable
  @Override
  public CssBlock getBlock() {
    return findChildByClass(CssBlock.class);
  }

  @NotNull
  @Override
  public CssProperty[] getProperties() {
    CssBlock block = getBlock();
    return block == null ? CssProperty.EMPTY_ARRAY : block.getProperties();
  }

  @Nullable
  @Override
  public CssSelectorDeclarationList getSelectorDeclarationList() {
    return findChildByClass(CssSelectorDeclarationList.class);
  }

  @Override
  @NotNull
  public CssSelectorDeclaration[] getSelectorDeclarations() {
    CssSelectorDeclarationList selectorReferenceList = getSelectorDeclarationList();
    return selectorReferenceList == null ? CssSelectorDeclaration.EMPTY_ARRAY : selectorReferenceList.getSelectorDeclarations();
  }
}
