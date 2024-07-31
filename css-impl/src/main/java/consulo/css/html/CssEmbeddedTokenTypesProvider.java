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

import consulo.annotation.component.ExtensionImpl;
import consulo.css.html.psi.CssHtmlElements;
import consulo.language.ast.IElementType;
import consulo.language.version.LanguageVersion;
import consulo.language.version.LanguageVersionUtil;
import consulo.xml.lexer.EmbeddedTokenTypesProvider;
import consulo.xml.lexer.HtmlLexer;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 2017-01-03
 */
@ExtensionImpl
public class CssEmbeddedTokenTypesProvider implements EmbeddedTokenTypesProvider {
    @Nonnull
    @Override
    public String getName() {
        return HtmlLexer.INLINE_STYLE_NAME;
    }

    @Nonnull
    @Override
    public IElementType getElementType() {
        return CssHtmlElements.MORPH_HTML_CSS_ELEMENT;
    }

    @Override
    public boolean isMyVersion(@Nonnull LanguageVersion languageVersion) {
        //noinspection RequiredXAction
        return languageVersion == LanguageVersionUtil.findDefaultVersion(languageVersion.getLanguage());
    }
}
