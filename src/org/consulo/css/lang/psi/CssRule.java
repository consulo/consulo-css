package org.consulo.css.lang.psi;

import com.intellij.lang.ASTNode;
import org.consulo.xstylesheet.psi.PsiXStyleSheetRule;
import org.jetbrains.annotations.NotNull;

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
