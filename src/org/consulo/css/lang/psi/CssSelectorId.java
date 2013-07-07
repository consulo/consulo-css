package org.consulo.css.lang.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import org.consulo.css.lang.CssTokens;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 07.07.13.
 */
public class CssSelectorId extends CssElement implements CssSelectorPart{
  public CssSelectorId(@NotNull ASTNode node) {
    super(node);
  }

  public String getOnlyName() {
    PsiElement childByType = findChildByType(CssTokens.IDENTIFIER);
    return childByType == null ? null : childByType.getText();
  }
}
