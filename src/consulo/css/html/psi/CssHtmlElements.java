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

package consulo.css.html.psi;

import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilderFactory;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.ILazyParseableElementType;
import consulo.css.html.lexer.CssInlineLexer;
import consulo.css.lang.CssLanguage;
import consulo.css.lang.CssElements;
import consulo.css.lang.CssTokens;
import consulo.css.lang.parser.CssParser;
import consulo.lang.LanguageVersion;

/**
 * @author VISTALL
 * @since 03-Jan-17
 */
public interface CssHtmlElements
{
	IElementType MORPH_HTML_CSS_ELEMENT = new ILazyParseableElementType("MORPH_HTML_CSS_ELEMENT", CssLanguage.INSTANCE)
	{
		@Override
		protected ASTNode doParseContents(@NotNull ASTNode chameleon, @NotNull PsiElement psi)
		{
			final Project project = psi.getProject();
			final Language languageForParser = getLanguageForParser(psi);
			final LanguageVersion tempLanguageVersion = chameleon.getUserData(LanguageVersion.KEY);
			final LanguageVersion languageVersion = tempLanguageVersion == null ? psi.getLanguageVersion() : tempLanguageVersion;
			CssInlineLexer lexer = new CssInlineLexer();

			final PsiBuilder builder = PsiBuilderFactory.getInstance().createBuilder(project, chameleon, lexer, languageForParser, languageVersion, chameleon.getChars());
			final CssParser parser = new CssParser();

			PsiBuilder.Marker mark = builder.mark();

			boolean inline = true;
			// if we can't parse as selector - rollback
			if(parser.parseSelectorDeclaration(builder) && builder.getTokenType() == CssTokens.LBRACE)
			{
				inline = false;
			}

			mark.rollbackTo();

			if(inline)
			{
				PsiBuilder.Marker rootMark = builder.mark();
				parseInlineRule(parser, builder);
				rootMark.done(CssElements.ROOT);
			}
			else
			{
				parser.parseRoot(builder);
			}

			return builder.getTreeBuilt();
		}

		private void parseInlineRule(@NotNull CssParser cssParser, @NotNull PsiBuilder builder)
		{
			PsiBuilder.Marker marker = builder.mark();

			PsiBuilder.Marker bodyMarker = builder.mark();

			while(!builder.eof())
			{
				if(builder.getTokenType() == CssTokens.IDENTIFIER)
				{
					PsiBuilder.Marker propertyMarker = builder.mark();

					builder.advanceLexer();

					if(CssParser.expect(builder, CssTokens.COLON, "':' expected"))
					{
						cssParser.parsePropertyValue(builder);
					}

					CssParser.expect(builder, CssTokens.SEMICOLON, builder.lookAhead(1) == CssTokens.IDENTIFIER ? "';' expected" : null);

					propertyMarker.done(CssElements.PROPERTY);
				}
				else
				{
					builder.error("unexpected symbol");
					builder.advanceLexer();
				}
			}

			bodyMarker.done(CssElements.BLOCK);

			marker.done(CssElements.RULE);
		}

		@Override
		protected Language getLanguageForParser(PsiElement psi)
		{
			return CssLanguage.INSTANCE;
		}
	};
}
