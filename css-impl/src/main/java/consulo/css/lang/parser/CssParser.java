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

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import consulo.css.lang.CssElements;
import consulo.css.lang.CssTokenSets;
import consulo.css.lang.CssTokens;
import consulo.lang.LanguageVersion;
import consulo.util.lang.StringUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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

	@Nonnull
	@Override
	public ASTNode parse(@Nonnull IElementType rootElementType, @Nonnull PsiBuilder builder, @Nonnull LanguageVersion languageVersion)
	{
		PsiBuilder.Marker mark = builder.mark();
		parseRoot(builder);
		mark.done(rootElementType);
		return builder.getTreeBuilt();
	}

	public void parseRoot(@Nonnull PsiBuilder builder)
	{
		PsiBuilder.Marker rootMarker = builder.mark();
		while(!builder.eof())
		{
			PsiBuilder.Marker marker = builder.mark();

			if(builder.getTokenType() == CssTokens.CHARSET_KEYWORD)
			{
				builder.advanceLexer();

				expect(builder, CssTokens.STRING, "Charset name expected");

				expect(builder, CssTokens.SEMICOLON, "Semicolon expected");

				marker.done(CssElements.CHARSET);
			}
			else if(builder.getTokenType() == CssTokens.IMPORT_KEYWORD)
			{
				builder.advanceLexer();

				if(!parseFunctionCall(builder))
				{
					expect(builder, CssTokens.STRING, "Expected import url");
				}

				expect(builder, CssTokens.SEMICOLON, "Semicolon expected");

				marker.done(CssElements.IMPORT);
			}
			else
			{
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
						if(parseProperty(builder, null) == null)
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
		}

		rootMarker.done(CssElements.ROOT);
	}

	@Nullable
	private PsiBuilder.Marker parseProperty(@Nonnull PsiBuilder builder, @Nullable PsiBuilder.Marker marker)
	{
		if(builder.getTokenType() == IDENTIFIER)
		{
			PsiBuilder.Marker propertyMarker = marker == null ? builder.mark() : marker;

			builder.advanceLexer();

			if(expect(builder, COLON, "':' expected"))
			{
				parsePropertyValue(builder);
			}

			if(builder.getTokenType() == IMPORTANT_KEYWORD)
			{
				builder.advanceLexer();
			}

			boolean last = builder.getTokenType() == null || builder.getTokenType() == CssTokens.RBRACE;

			expect(builder, SEMICOLON, last ? null : "';' expected");

			propertyMarker.done(PROPERTY);

			return propertyMarker;
		}
		else if(builder.getTokenType() == CssTokens.ASTERISK)
		{
			PsiBuilder.Marker propertyMarker = builder.mark();

			builder.advanceLexer();

			if(parseProperty(builder, propertyMarker) == null)
			{
				propertyMarker.error("Expected name");
				return null;
			}
			return propertyMarker;
		}
		else
		{
			return null;
		}
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
			else if(type == SEMICOLON || type == RBRACE || type == IMPORTANT_KEYWORD)
			{
				break;
			}
			else
			{
				if(valueMarker == null)
				{
					valueMarker = builder.mark();
				}

				if(parseFunctionCall(builder))
				{
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

	private boolean parseFunctionCall(PsiBuilder builder)
	{
		IElementType type = builder.getTokenType();
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
			return true;
		}
		return false;
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

		while(parseSimpleSelector(builder))
		{
		}

		while(builder.getTokenType() == CssTokens.PLUS || builder.getTokenType() == CssTokens.TILDE || builder.getTokenType() == CssTokens.GT)
		{
			builder.advanceLexer();

			if(parseSelector(builder) == null)
			{
				builder.error("Selector expected");
				break;
			}
		}

		mark.done(SELECTOR);
		return mark;
	}

	private boolean parseSimpleSelector(PsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();
		boolean isSelector;
		if((isSelector = parseSelectorClassOrId(builder, false)) || builder.getTokenType() == CssTokens.ASTERISK || builder.getTokenType() == CssTokens.IDENTIFIER)
		{
			if(isSelector)
			{
				parseSelectorClassOrId(builder, true);
			}
			else
			{
				builder.advanceLexer();
			}

			PsiBuilder.Marker suffixListMarker = builder.mark();

			parseSelectorAttributeList(builder);

			parseSelectorPseudoClass(builder);

			while(!builder.eof())
			{
				if(parseSelectorClassOrId(builder, false))
				{
					PsiBuilder.Marker suffixMarker = builder.mark();

					parseSelectorClassOrId(builder, true);

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

	private boolean parseSelectorClassOrId(PsiBuilder builder, boolean eat)
	{
		if(builder.getTokenType() == CssTokens.DOT && builder.lookAhead(1) == CssTokens.IDENTIFIER)
		{
			if(eat)
			{
				PsiBuilder.Marker mark = builder.mark();
				builder.advanceLexer();
				builder.advanceLexer();
				mark.collapse(CssTokens.SELECTOR_CLASS);
			}

			return true;
		}
		else if(builder.getTokenType() == CssTokens.SHARP && builder.lookAhead(1) == CssTokens.IDENTIFIER)
		{
			if(eat)
			{
				PsiBuilder.Marker mark = builder.mark();
				builder.advanceLexer();
				builder.advanceLexer();
				mark.collapse(CssTokens.SELECTOR_ID);
			}

			return true;
		}
		return false;
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

			if(CssTokenSets.SELECTOR_ATTRIBUTE_LIST_EQ.contains(builder.getTokenType()))
			{
				builder.advanceLexer();

				if(builder.getTokenType() == STRING || builder.getTokenType() == IDENTIFIER)
				{
					builder.advanceLexer();
				}
				else
				{
					builder.error("Attribute value expected");
				}
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
		if(builder.getTokenType() == COLON && builder.lookAhead(1) == IDENTIFIER && builder.lookAhead(2) == LPAR)
		{
			builder.advanceLexer();

			parseFunctionCall(builder);

			return;
		}

		while(builder.getTokenType() == COLON || builder.getTokenType() == COLONCOLON)
		{
			PsiBuilder.Marker marker = builder.mark();

			builder.advanceLexer();

			CharSequence name = builder.getTokenType() == IDENTIFIER ? builder.getTokenSequence() : null;

			if(expect(builder, IDENTIFIER, "Identifier expected"))
			{
				if(builder.getTokenType() == LPAR)
				{
					PsiBuilder.Marker argsMarker = builder.mark();
					
					builder.advanceLexer();

					if(StringUtil.equals(name, "not"))
					{
						if(parseSelector(builder) == null)
						{
							builder.error("Selector expected");
						}
					}

					expect(builder, RPAR, "')' expected");

					argsMarker.done(SELECTOR_PSEUDO_CLASS_ARGUMENT_LIST);
				}
			}

			marker.done(SELECTOR_PSEUDO_CLASS);
		}
	}
}
