package org.consulo.css.lang.psi.reference.nameResolving;

import com.intellij.psi.PsiElement;
import org.consulo.css.lang.psi.CssSelectorReference;
import org.consulo.xstylesheet.psi.reference.nameResolving.XStyleRuleCondition;

/**
 * @author VISTALL
 * @since 07.07.13.
 */
public class CssSimpleRuleOnlyTypeCondition implements XStyleRuleCondition {
  private CssSimpleRuleConditionType myConditionType;


  public CssSimpleRuleOnlyTypeCondition(CssSimpleRuleConditionType conditionType) {
    myConditionType = conditionType;
  }

  @Override
  public boolean isAccepted(PsiElement psiElement) {
    if (psiElement instanceof CssSelectorReference) {
      CssSelectorReference selectorReference = (CssSelectorReference) psiElement;

      switch (myConditionType) {
        case ID:
          return selectorReference.isIdRule();
        case CLASS:
          return selectorReference.isClassRule();
      }
    }

    return false;
  }
}
