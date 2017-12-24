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

package consulo.css.lang.parser;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import consulo.css.lang.CssElements;
import consulo.css.lang.CssTokens;
import consulo.lang.LanguageVersion;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class CssParser implements PsiParser, CssTokens, CssElements
{
	public static boolean expect(PsiBuilder builder, IElementType elementType, @Nullable String message)
	{
		if(builder.getTokenType() != elementType)
		{
			if(message != null)
			{
				builder.error(message);
			}
			return false;
		}
		else
		{
			builder.advanceLexer();
			return true;
		}
	}

	@NotNull
	@Override
	public ASTNode parse(@NotNull IElementType rootElementType, @NotNull PsiBuilder builder, @NotNull LanguageVersion languageVersion)
	{
		PsiBuilder.Marker mark = builder.mark();
		parseRoot(builder);
		mark.done(rootElementType);
		return builder.getTreeBuilt();
	}

	public void parseRoot(@NotNull PsiBuilder builder)
	{
		PsiBuilder.Marker rootMarker = builder.mark();
		while(!builder.eof())
		{
			PsiBuilder.Marker marker = builder.mark();

			if(!parseSelectorListNew(builder))
			{
				builder.error("Selector expected");
			}

			if(builder.getTokenType() == LBRACE)
			{
				PsiBuilder.Marker bodyMarker = builder.mark();
				builder.advanceLexer();

				while(!builder.eof())
				{
					if(builder.getTokenType() == IDENTIFIER)
					{
						PsiBuilder.Marker propertyMarker = builder.mark();

						builder.advanceLexer();

						if(expect(builder, COLON, "':' expected"))
						{
							parsePropertyValue(builder);
						}

						if(builder.getTokenType() == IMPORTANT)
						{
							builder.advanceLexer();
						}

						boolean last = builder.lookAhead(1) == null || builder.lookAhead(1) == CssTokens.RBRACE;

						expect(builder, SEMICOLON, last ? null : "';' expected");

						propertyMarker.done(PROPERTY);
					}
					else
					{
						break;
					}
				}

				expect(builder, RBRACE, "'}' expected");

				bodyMarker.done(BLOCK);
			}
			else
			{
				builder.error("'{' expected");
			}

			marker.done(RULE);
		}

		rootMarker.done(CssElements.ROOT);
	}

	public void parsePropertyValue(PsiBuilder builder)
	{
		PsiBuilder.Marker valueMarker = null;

		while(!builder.eof())
		{
			IElementType type = builder.getTokenType();
			if(type == COMMA)
			{
				if(valueMarker != null)
				{
					valueMarker.done(PROPERTY_VALUE_PART);
					valueMarker = null;
				}

				builder.advanceLexer();
			}
			else if(type == SEMICOLON || type == RBRACE || type == IMPORTANT)
			{
				break;
			}
			else
			{
				if(valueMarker == null)
				{
					valueMarker = builder.mark();
				}

				if(type == CssTokens.IDENTIFIER && builder.lookAhead(1) == CssTokens.LPAR)
				{
					PsiBuilder.Marker functionMarker = builder.mark();

					builder.advanceLexer();

					PsiBuilder.Marker argumentList = builder.mark();
					if(expect(builder, LPAR, "'(' expected"))
					{
						boolean noArgument = true;
						while(!builder.eof())
						{
							IElementType tokenType = builder.getTokenType();
							if(tokenType == COMMA)
							{

								if(noArgument)
								{
									builder.error("Argument expected");
									break;
								}

								builder.advanceLexer();
								noArgument = true;
							}
							else if(tokenType == RPAR)
							{
								if(noArgument)
								{
									builder.error("Argument expected");
								}
								break;
							}
							else
							{
								builder.advanceLexer();
								noArgument = false;
							}
						}
						expect(builder, RPAR, "')' expected");
					}
					argumentList.done(FUNCTION_CALL_PARAMETER_LIST);
					functionMarker.done(FUNCTION_CALL);
					continue;
				}

				if(type == CssTokens.BAD_CHARACTER)
				{
					PsiBuilder.Marker mark = builder.mark();
					builder.advanceLexer();
					mark.error("Bad token");
				}
				else
				{
					builder.advanceLexer();
				}
			}
		}

		if(valueMarker != null)
		{
			valueMarker.done(PROPERTY_VALUE_PART);
		}
	}

	public boolean parseSelectorListNew(PsiBuilder builder)
	{
		PsiBuilder.Marker listMarker = builder.mark();

		boolean errorSelector = false;

		PsiBuilder.Marker current = null;
		boolean first = true;
		boolean comma = false;
		while(!builder.eof())
		{
			if(builder.getTokenType() == LBRACE)
			{
				if(first)
				{
					errorSelector = true;
				}
				break;
			}

			if(!first)
			{
				comma = expect(builder, CssTokens.COMMA, "Comma expected");
			}

			PsiBuilder.Marker marker = parseSelector(builder);

			current = marker;

			if(marker == null)
			{
				if(first)
				{
					errorSelector = true;
				}
			}

			if(marker == null)
			{
				if(builder.getTokenType() == CssTokens.LBRACE)
				{
					break;
				}
				else
				{
					markError(builder, "Unexpected token");
				}
			}
			first = false;
		}
		if(comma && current == null)
		{
			builder.error("Selector expected");
		}
		listMarker.done(SELECTOR_LIST);
		return !errorSelector;
	}

	private void markError(PsiBuilder builder, String message)
	{
		PsiBuilder.Marker mark = builder.mark();
		builder.advanceLexer();
		mark.error(message);
	}

	@Nullable
	public PsiBuilder.Marker parseSelector(PsiBuilder builder)
	{
		PsiBuilder.Marker mark = builder.mark();
		if(!parseSimpleSelector(builder))
		{
			mark.drop();
			return null;
		}

		while(builder.getTokenType() == CssTokens.IDENTIFIER)
		{
			parseSimpleSelector(builder);
		}

		if(builder.getTokenType() == CssTokens.PLUS || builder.getTokenType() == CssTokens.TILDE || builder.getTokenType() == CssTokens.GT)
		{
			builder.advanceLexer();

			if(!parseSimpleSelector(builder))
			{
				builder.error("Selector expected");
			}
		}

		mark.done(SELECTOR);
		return mark;
	}

	private boolean parseSimpleSelector(PsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();
		if(builder.getTokenType() == CssTokens.SELECTOR_CLASS || builder.getTokenType() == CssTokens.SELECTOR_ID || builder.getTokenType() == CssTokens.ASTERISK || builder.getTokenType() ==
				CssTokens.IDENTIFIER)
		{
			builder.advanceLexer();

			PsiBuilder.Marker suffixListMarker = builder.mark();

			parseSelectorAttributeList(builder);

			parseSelectorPseudoClass(builder);

			while(!builder.eof())
			{
				if(builder.getTokenType() == CssTokens.SELECTOR_CLASS || builder.getTokenType() == CssTokens.SELECTOR_ID)
				{
					PsiBuilder.Marker suffixMarker = builder.mark();

					builder.advanceLexer();

					parseSelectorPseudoClass(builder);

					suffixMarker.done(CssElements.SIMPLE_SELECTOR);
				}
				else
				{
					break;
				}
			}
			suffixListMarker.done(CssElements.SELECTOR_SUFFIX_LIST);

			marker.done(CssElements.SIMPLE_SELECTOR);
			return true;
		}
		else if(builder.getTokenType() == LBRACKET)
		{
			parseSelectorAttributeList(builder);

			parseSelectorPseudoClass(builder);

			marker.done(CssElements.SIMPLE_SELECTOR);
			return true;
		}
		else if(builder.getTokenType() == CssTokens.COLON || builder.getTokenType() == CssTokens.COLONCOLON)
		{
			parseSelectorPseudoClass(builder);

			marker.done(CssElements.SIMPLE_SELECTOR);
			return true;
		}
		else
		{
			marker.drop();
			return false;
		}
	}

	private void parseSelectorAttributeList(PsiBuilder builder)
	{
		if(builder.getTokenType() == LBRACKET)
		{
			PsiBuilder.Marker marker = builder.mark();

			builder.advanceLexer();

			while(parseSelectorAttribute(builder))
			{
				if(builder.getTokenType() == COMMA)
				{
					builder.advanceLexer();
				}
				else if(builder.getTokenType() == RBRACKET)
				{
					break;
				}
			}

			expect(builder, RBRACKET, "']' expected");

			marker.done(SELECTOR_ATTRIBUTE_LIST);
		}
	}

	private boolean parseSelectorAttribute(PsiBuilder builder)
	{
		if(builder.getTokenType() == IDENTIFIER)
		{
			PsiBuilder.Marker mark = builder.mark();

			builder.advanceLexer();

			if(expect(builder, EQ, null))
			{
				expect(builder, STRING, "Attribute value expected");
			}
			mark.done(SELECTOR_ATTRIBUTE);
			return true;
		}
		else if(builder.getTokenType() == RBRACKET)
		{
		}
		else
		{
			builder.error("Identifier expected");
		}
		return false;
	}

	private void parseSelectorPseudoClass(PsiBuilder builder)
	{
		while(builder.getTokenType() == COLON || builder.getTokenType() == COLONCOLON)
		{
			PsiBuilder.Marker marker = builder.mark();

			builder.advanceLexer();

			expect(builder, IDENTIFIER, "Identifier expected");

			marker.done(SELECTOR_PSEUDO_CLASS);
		}
	}
}
