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

package org.consulo.css.lang.parser;

import org.consulo.css.lang.CssPsiTokens;
import org.consulo.css.lang.CssTokens;
import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;
import com.intellij.lang.LanguageVersion;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class CssParser implements PsiParser, CssTokens, CssPsiTokens
{
	private static boolean expect(PsiBuilder builder, IElementType elementType, String message)
	{
		if(builder.getTokenType() != elementType)
		{
			builder.error(message);
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
	public ASTNode parse(@NotNull IElementType iElementType, @NotNull PsiBuilder builder, @NotNull LanguageVersion languageVersion)
	{
		PsiBuilder.Marker mark = builder.mark();
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

						if(builder.getTokenType() != RBRACE)
						{
							expect(builder, SEMICOLON, "';' expected");
						}

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
		mark.done(iElementType);
		return builder.getTreeBuilt();
	}

	private void parsePropertyValue(PsiBuilder builder)
	{
		PsiBuilder.Marker valueMarker = null;

		while(!builder.eof())
		{
			if(builder.getTokenType() == COMMA)
			{
				if(valueMarker != null)
				{
					valueMarker.done(PROPERTY_VALUE_PART);
					valueMarker = null;
				}

				builder.advanceLexer();
			}
			else if(builder.getTokenType() == SEMICOLON || builder.getTokenType() == RBRACE)
			{
				break;
			}
			else if(builder.getTokenType() == FUNCTION_NAME)
			{
				if(valueMarker == null)
				{
					valueMarker = builder.mark();
				}

				PsiBuilder.Marker functionMarker = builder.mark();

				builder.advanceLexer();

				PsiBuilder.Marker argumentList = builder.mark();
				if(expect(builder, LPAR, "'(' expected"))
				{
					boolean noArgument = true;
					while(true) {
						IElementType tokenType = builder.getTokenType();
						if(tokenType == COMMA) {

							if(noArgument) {
								builder.error("Argument expected");
								break;
							}

							builder.advanceLexer();
							noArgument = true;
						}
						else if(tokenType == RPAR) {
							if(noArgument) {
								builder.error("Argument expected");
							}
							break;
						}
						else {
							builder.advanceLexer();
							noArgument = false;
						}
					}
				}
				expect(builder, RPAR, "')' expected");
				argumentList.done(FUNCTION_CALL_PARAMETER_LIST);

				functionMarker.done(FUNCTION_CALL);
			}
			else
			{
				if(valueMarker == null)
				{
					valueMarker = builder.mark();
				}
				builder.advanceLexer();
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

	private boolean parseSelectorDeclaration(PsiBuilder builder)
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

			if(expect(builder, EQ, "'=' expected"))
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
		if(builder.getTokenType() == COLON || builder.getTokenType() == COLONCOLON)
		{
			PsiBuilder.Marker marker = builder.mark();

			builder.advanceLexer();

			expect(builder, IDENTIFIER, "Identifier expected");

			marker.done(SELECTOR_PSEUDO_CLASS);
		}
	}
}
