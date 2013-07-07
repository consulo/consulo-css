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
public class CssSimpleRuleOnlyTypeCondition implements XStyleRuleCondition{
  private CssSimpleRuleConditionType myConditionType;


  public CssSimpleRuleOnlyTypeCondition(CssSimpleRuleConditionType conditionType) {
    myConditionType = conditionType;
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
      switch (myConditionType) {
        case ID:
          type = CssPsiTokens.SELECTOR_ID;
          break;
        case CLASS:
          type = CssPsiTokens.SELECTOR_CLASS;
          break;
      }
      return selectorPart.getNode().getElementType() == type;
    }
    else {
      return false;
    }
  }
}
