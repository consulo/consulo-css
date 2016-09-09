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

package consulo.css.lang;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;

/**
 * @author VISTALL
 * @since 02.07.13
 */
public interface CssTokens extends TokenType
{
	IElementType BLOCK_COMMENT = new CssElementType("BLOCK_COMMENT");

	IElementType IDENTIFIER = new CssElementType("IDENTIFIER");
	IElementType STRING = new CssElementType("STRING");
	IElementType NUMBER = new CssElementType("NUMBER");
	IElementType SHARP = new CssElementType("SHARP");
	IElementType LPAR = new CssElementType("LPAR");
	IElementType RPAR = new CssElementType("RPAR");
	IElementType LBRACE = new CssElementType("LBRACE");
	IElementType RBRACE = new CssElementType("RBRACE");
	IElementType LBRACKET = new CssElementType("LBRACKET");
	IElementType RBRACKET = new CssElementType("RBRACKET");
	IElementType COLON = new CssElementType("COLON");
	IElementType COLONCOLON = new CssElementType("COLONCOLON");
	IElementType EQ = new CssElementType("EQ");
	IElementType GE = new CssElementType("GE");
	IElementType SEMICOLON = new CssElementType("SEMICOLON");
	IElementType COMMA = new CssElementType("COMMA");
	IElementType ASTERISK = new CssElementType("ASTERISK");
	IElementType DOT = new CssElementType("DOT");
	IElementType PLUS = new CssElementType("PLUS");
	IElementType PERC = new CssElementType("PERC");

	IElementType FUNCTION_NAME = new CssElementType("FUNCTION_NAME");
	IElementType FUNCTION_ARGUMENT = new CssElementType("FUNCTION_ARGUMENT");
}
