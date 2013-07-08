package org.consulo.css.lang;

import com.intellij.lang.injection.MultiHostInjector;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlAttributeValue;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * @author VISTALL
 * @since 08.07.13.
 */
public class CssMultiHostInjector implements MultiHostInjector {
  @Override
  public void getLanguagesToInject(@NotNull MultiHostRegistrar multiHostRegistrar, @NotNull PsiElement psiElement) {
    XmlAttribute parent = (XmlAttribute) psiElement.getParent();
    if(!"style".equals(parent.getName())) {
      return;
    }

    int textLength = psiElement.getTextLength();
    if(textLength <= 2) {
      return;
    }
    multiHostRegistrar.startInjecting(CssLanguage.INSTANCE).addPlace("style {", "}", (PsiLanguageInjectionHost) psiElement, new TextRange(1, textLength - 1)).doneInjecting();
  }

  @NotNull
  @Override
  public List<? extends Class<? extends PsiElement>> elementsToInjectIn() {
    return Arrays.asList(XmlAttributeValue.class);
  }
}
