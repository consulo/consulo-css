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

	IElementType SELECTOR_ID = new CssElementType("SELECTOR_ID");
	IElementType SELECTOR_CLASS = new CssElementType("SELECTOR_CLASS");

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
	IElementType GT = new CssElementType("GT");
	IElementType SEMICOLON = new CssElementType("SEMICOLON");
	IElementType COMMA = new CssElementType("COMMA");
	IElementType ASTERISK = new CssElementType("ASTERISK");
	IElementType DOT = new CssElementType("DOT");
	IElementType PLUS = new CssElementType("PLUS");
	IElementType TILDE = new CssElementType("TILDE");
	IElementType PERC = new CssElementType("PERC");

	IElementType URL_TOKEN = new CssElementType("URL_TOKEN");

	IElementType IMPORTANT_KEYWORD = new CssElementType("IMPORTANT_KEYWORD");
	IElementType PAGE_KEYWORD = new CssElementType("PAGE_KEYWORD");
	IElementType MEDIA_KEYWORD = new CssElementType("MEDIA_KEYWORD");
	IElementType IMPORT_KEYWORD = new CssElementType("IMPORT_KEYWORD");
	IElementType CHARSET_KEYWORD = new CssElementType("CHARSET_KEYWORD");
	IElementType NAMESPACE_KEYWORD = new CssElementType("NAMESPACE_KEYWORD");
	IElementType FONT_FACE_KEYWORD = new CssElementType("FONT_FACE_KEYWORD");
}
