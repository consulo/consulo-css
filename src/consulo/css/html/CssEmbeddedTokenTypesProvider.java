/*
 * Copyright 2013-2017 must-be.org
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

package consulo.css.html;

import org.jetbrains.annotations.NotNull;
import com.intellij.lexer.EmbeddedTokenTypesProvider;
import com.intellij.lexer.HtmlLexer;
import com.intellij.psi.tree.IElementType;
import consulo.css.html.psi.CssHtmlElements;
import consulo.lang.LanguageVersion;
import consulo.lang.util.LanguageVersionUtil;

/**
 * @author VISTALL
 * @since 03-Jan-17
 */
public class CssEmbeddedTokenTypesProvider implements EmbeddedTokenTypesProvider
{
	@NotNull
	@Override
	public String getName()
	{
		return HtmlLexer.INLINE_STYLE_NAME;
	}

	@NotNull
	@Override
	public IElementType getElementType()
	{
		return CssHtmlElements.MORPH_HTML_CSS_ELEMENT;
	}

	@Override
	public boolean isMyVersion(@NotNull LanguageVersion languageVersion)
	{
		//noinspection RequiredXAction
		return languageVersion == LanguageVersionUtil.findDefaultVersion(languageVersion.getLanguage());
	}
}
