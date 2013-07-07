package org.consulo.css.html.psi.reference.classOrId;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.psi.xml.XmlTokenType;
import com.intellij.util.ProcessingContext;
import org.consulo.css.lang.psi.reference.nameResolving.CssSimpleRuleConditionType;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 07.07.13.
 */
public class HtmlIdOrClassToCssFileReferenceProvider extends PsiReferenceProvider {
  private final CssSimpleRuleConditionType myCssRefTo;

  public HtmlIdOrClassToCssFileReferenceProvider(CssSimpleRuleConditionType cssRefTo) {
    myCssRefTo = cssRefTo;
  }

  @NotNull
  @Override
  public PsiReference[] getReferencesByElement(@NotNull PsiElement psiElement, @NotNull ProcessingContext processingContext) {
    ASTNode value = psiElement.getNode().findChildByType(XmlTokenType.XML_ATTRIBUTE_VALUE_TOKEN);
    if (value == null) {
      return PsiReference.EMPTY_ARRAY;
    }

    return new PsiReference[] {new HtmlIdOrClassToCssFileReference(value.getPsi(), myCssRefTo)};
  }
}
