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

import com.intellij.lexer.HtmlEmbeddedTokenTypesProvider;
import com.intellij.psi.tree.IElementType;
import consulo.css.html.psi.CssHtmlTokens;

/**
 * @author VISTALL
 * @since 03-Jan-17
 */
public class CssHtmlEmbeddedTokenTypesProvider implements HtmlEmbeddedTokenTypesProvider
{
	@Override
	public String getName()
	{
		return "style";
	}

	@Override
	public IElementType getElementType()
	{
		return CssHtmlTokens.HTML_CSS_ELEMENT;
	}

	@Override
	public IElementType getInlineElementType()
	{
		return CssHtmlTokens.INLINE_HTML_CSS_ELEMENT;
	}
}
