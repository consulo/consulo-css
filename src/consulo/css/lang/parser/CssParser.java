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

			if(!parseSelectorList(builder))
			{
				builder.advanceLexer();
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
					}
					expect(builder, RPAR, "')' expected");
					argumentList.done(FUNCTION_CALL_PARAMETER_LIST);

					functionMarker.done(FUNCTION_CALL);
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

	private boolean parseSelectorList(PsiBuilder builder)
	{
		PsiBuilder.Marker listMarker = builder.mark();

		boolean empty = true;
		while(true)
		{
			boolean b = parseSelectorDeclaration(builder);
			if(!b)
			{
				break;
			}
			else
			{
				empty = false;
			}

			if(builder.getTokenType() == COMMA)
			{
				builder.advanceLexer();
			}
			else if(builder.getTokenType() == LBRACE)
			{
				break;
			}
		}

		if(empty)
		{
			builder.error("Expected '*', '.',  '#'. identifier or '['");
		}
		listMarker.done(SELECTOR_DECLARATION_LIST);
		return !empty;
	}

	public boolean parseSelectorDeclaration(PsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();

		boolean empty = true;
		while(true)
		{
			boolean b = parseSelectorReference(builder);
			if(!b)
			{
				break;
			}
			else
			{
				empty = false;
			}
		}

		if(empty)
		{
			marker.drop();
		}
		else
		{
			marker.done(SELECTOR_DECLARATION);
		}
		return !empty;
	}

	private boolean parseSelectorReference(PsiBuilder builder)
	{
		PsiBuilder.Marker marker = builder.mark();

		if(builder.getTokenType() == IDENTIFIER || builder.getTokenType() == ASTERISK)
		{
			builder.advanceLexer();

			parseSelectorAttributeList(builder);

			parseSelectorPseudoClass(builder);

			marker.done(SELECTOR_REFERENCE);
			return true;
		}
		else if(builder.getTokenType() == DOT || builder.getTokenType() == SHARP || builder.getTokenType() == GE || builder.getTokenType() == PLUS)
		{
			builder.advanceLexer();

			expect(builder, IDENTIFIER, "Identifier expected");

			parseSelectorAttributeList(builder);

			parseSelectorPseudoClass(builder);

			marker.done(SELECTOR_REFERENCE);
			return true;
		}
		else if(builder.getTokenType() == LBRACKET)
		{
			parseSelectorAttributeList(builder);

			marker.done(SELECTOR_REFERENCE);
			return true;
		}

		marker.drop();
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
