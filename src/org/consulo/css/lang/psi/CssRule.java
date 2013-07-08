package org.consulo.css.lang.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
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

  @NotNull
  public CssSelectorReference getSelectorReference() {
    return findNotNullChildByClass(CssSelectorReference.class);
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

  @Override
  public PsiElement getOnlyNameIdentifier() {
    return getSelectorReference().getSelectorPart();
  }

  @Override
  public boolean isIdRule() {
    CssSelectorReference selectorReference = getSelectorReference();
    CssSelectorPart selectorPart = selectorReference.getSelectorPart();

    return selectorPart instanceof CssSelectorId;
  }

  @Override
  public boolean isClassRule() {
    CssSelectorReference selectorReference = getSelectorReference();
    CssSelectorPart selectorPart = selectorReference.getSelectorPart();

    return selectorPart instanceof CssSelectorClass;
  }
}
