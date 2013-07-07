package org.consulo.css.html;

import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceRegistrar;
import com.intellij.xml.util.XmlUtil;
import org.consulo.css.html.psi.reference.classOrId.HtmlIdOrClassToCssFileReferenceProvider;
import org.consulo.css.html.psi.reference.file.HtmlHrefToCssFileReferenceProvider;
import org.consulo.css.lang.psi.reference.nameResolving.CssSimpleRuleConditionType;

/**
 * @author VISTALL
 * @since 07.07.13.
 */
public class CssToHtmlReferenceContributor extends PsiReferenceContributor{
  @Override
  public void registerReferenceProviders(PsiReferenceRegistrar psiReferenceRegistrar) {
    XmlUtil.registerXmlAttributeValueReferenceProvider(psiReferenceRegistrar, new String[]{"id"}, null, false, new HtmlIdOrClassToCssFileReferenceProvider(CssSimpleRuleConditionType.ID));

    XmlUtil.registerXmlAttributeValueReferenceProvider(psiReferenceRegistrar, new String[]{"class"}, null, false, new HtmlIdOrClassToCssFileReferenceProvider(CssSimpleRuleConditionType.CLASS));

    HtmlHrefToCssFileReferenceProvider provider = new HtmlHrefToCssFileReferenceProvider();
    XmlUtil.registerXmlAttributeValueReferenceProvider(psiReferenceRegistrar, new String[]{"href"}, provider.getElementFilter(), false, provider);
  }
}
