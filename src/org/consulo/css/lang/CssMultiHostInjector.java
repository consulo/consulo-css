package org.consulo.css.lang;

import com.intellij.lang.injection.MultiHostInjector;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.xml.XmlAttribute;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 08.07.13.
 */
public class CssMultiHostInjector implements MultiHostInjector {
	@Override
	public void injectLanguages(@NotNull MultiHostRegistrar multiHostRegistrar, @NotNull PsiElement element) {
		PsiElement parent = element.getParent();
		if(!(parent instanceof XmlAttribute)) {
			return;
		}

		if (!"style".equals(((XmlAttribute)parent).getName())) {
			return;
		}

		int textLength = element.getTextLength();
		if (textLength <= 2) {
			return;
		}
		multiHostRegistrar.startInjecting(CssLanguage.INSTANCE).addPlace("style {", "}", (PsiLanguageInjectionHost) element, new TextRange(1, textLength - 1)).doneInjecting();
	}
}
