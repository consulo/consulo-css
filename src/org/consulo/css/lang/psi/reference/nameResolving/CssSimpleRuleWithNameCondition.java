package org.consulo.css.lang.psi.reference.nameResolving;

import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import org.consulo.css.lang.CssPsiTokens;
import org.consulo.css.lang.psi.CssRule;
import org.consulo.css.lang.psi.CssSelectorPart;
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
    if(psiElement instanceof CssRule) {
      CssSelectorReference selectorReference = ((CssRule) psiElement).getSelectorReference();

      CssSelectorPart selectorPart = selectorReference.getSelectorPart();
      if(selectorPart == null) {
        return false;
      }

      String onlyName = selectorPart.getOnlyName();
      if(onlyName == null) {
        return false;
      }

      IElementType type = null;
      switch (getConditionType()) {
        case ID:
          type = CssPsiTokens.SELECTOR_ID;
          break;
        case CLASS:
          type = CssPsiTokens.SELECTOR_CLASS;
          break;
      }
      if(selectorPart.getNode().getElementType() != type) {
        return false;
      }
      return onlyName.equals(myName);
    }
    else {
      return false;
    }
  }

  public CssSimpleRuleConditionType getConditionType() {
    return myConditionType;
  }
}
