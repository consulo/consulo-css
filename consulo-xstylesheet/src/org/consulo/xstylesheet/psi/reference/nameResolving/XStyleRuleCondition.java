package org.consulo.xstylesheet.psi.reference.nameResolving;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiElementFilter;

/**
 * @author VISTALL
 * @since 07.07.13.
 */
public interface XStyleRuleCondition extends PsiElementFilter {
  XStyleRuleCondition ANY = new XStyleRuleCondition() {
    @Override
    public boolean isAccepted(PsiElement psiElement) {
      return true;
    }
  };
}
