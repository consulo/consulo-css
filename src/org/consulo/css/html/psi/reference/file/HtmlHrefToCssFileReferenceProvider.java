package org.consulo.css.html.psi.reference.file;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.Condition;
import com.intellij.psi.*;
import com.intellij.psi.filters.ElementFilter;
import com.intellij.psi.filters.ElementFilterBase;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReferenceSet;
import com.intellij.psi.util.PsiElementFilter;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlTokenType;
import com.intellij.util.ProcessingContext;
import org.consulo.css.lang.psi.CssFile;
import org.jetbrains.annotations.NotNull;


/**
 * @author VISTALL
 * @since 07.07.13.
 */
public class HtmlHrefToCssFileReferenceProvider extends PsiReferenceProvider {
  public static class CssFileHrefFilter extends ElementFilterBase<XmlAttributeValue> implements PsiElementFilter {
    public CssFileHrefFilter() {
      super(XmlAttributeValue.class);
    }

    @Override
    protected boolean isElementAcceptable(XmlAttributeValue xmlAttributeValue, PsiElement psiElement) {
      XmlAttribute xmlAttribute = (XmlAttribute) xmlAttributeValue.getParent();
      if (!"href".equalsIgnoreCase(xmlAttribute.getName())) {
        return false;
      }
      XmlTag xmlTag = xmlAttribute.getParent();//XmlAttribute -> XmlTag

      if ("link".equalsIgnoreCase(xmlTag.getName())) {
        String rel = xmlTag.getAttributeValue("rel");
        if (!"stylesheet".equalsIgnoreCase(rel)) {
          return false;
        }

        String type = xmlTag.getAttributeValue("type");
        if (!"text/css".equalsIgnoreCase(type)) {
          return false;
        }
      }
      return true;
    }

    @Override
    public boolean isAccepted(PsiElement psiElement) {
      return psiElement instanceof XmlAttributeValue && isElementAcceptable((XmlAttributeValue) psiElement, psiElement);
    }
  }

  @NotNull
  @Override
  public PsiReference[] getReferencesByElement(@NotNull PsiElement psiElement, @NotNull ProcessingContext processingContext) {
    XmlAttributeValue xmlAttributeValue = (XmlAttributeValue) psiElement;
    ASTNode value = xmlAttributeValue.getNode().findChildByType(XmlTokenType.XML_ATTRIBUTE_VALUE_TOKEN);
    if (value == null) {
      return PsiReference.EMPTY_ARRAY;
    }
    FileReferenceSet fileReferenceSet = new FileReferenceSet(value.getPsi()) {
      @Override
      protected Condition<PsiFileSystemItem> getReferenceCompletionFilter() {
        return new Condition<PsiFileSystemItem>() {
          @Override
          public boolean value(PsiFileSystemItem psiFileSystemItem) {
            return psiFileSystemItem instanceof CssFile || psiFileSystemItem instanceof PsiDirectory;
          }
        };
      }
    };
    fileReferenceSet.setEmptyPathAllowed(false);

    return fileReferenceSet.getAllReferences();
  }

  public ElementFilter getElementFilter() {
    return new CssFileHrefFilter();
  }
}
