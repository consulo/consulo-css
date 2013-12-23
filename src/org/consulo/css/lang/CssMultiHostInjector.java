/*
 * Copyright 2013 must-be.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.consulo.css.lang;

import org.jetbrains.annotations.NotNull;
import com.intellij.lang.injection.MultiHostInjector;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.xml.XmlAttribute;

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
