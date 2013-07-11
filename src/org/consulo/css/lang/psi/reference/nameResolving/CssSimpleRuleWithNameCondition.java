package org.consulo.css.lang.psi.reference.nameResolving;

import com.intellij.psi.PsiElement;
import org.consulo.css.lang.psi.CssSelectorReference;
import org.consulo.xstylesheet.psi.reference.nameResolving.XStyleRuleCondition;

/**
 * @author VISTALL
 * @since 07.07.13.
 */
public class CssSimpleRuleWithNameCondition implements XStyleRuleCondition{
  private CssSimpleRuleConditionType myConditionType;
  private String myName;

  public CssSimpleRuleWithNameCondition(CssSimpleRuleConditionType conditionType, String name) {
    myConditionType = conditionType;
    myName = name;
  }

  @Override
  public boolean isAccepted(PsiElement psiElement) {
    if (psiElement instanceof CssSelectorReference) {
      CssSelectorReference selectorReference = (CssSelectorReference) psiElement;

      if(!myName.equals(selectorReference.getName())) {
        return false;
      }

      switch (myConditionType) {
        case ID:
          return selectorReference.isIdRule();
        case CLASS:
          return selectorReference.isClassRule();
      }
    }

    return false;
  }

  public CssSimpleRuleConditionType getConditionType() {
    return myConditionType;
  }
}
