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

import consulo.language.ast.TokenSet;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public interface CssTokenSets
{
	TokenSet WHITE_SPACES = TokenSet.create(CssTokens.WHITE_SPACE);

	TokenSet STRINGS = TokenSet.create(CssTokens.STRING);

	TokenSet COMMENTS = TokenSet.create(CssTokens.BLOCK_COMMENT);

	TokenSet SELECTOR_ATTRIBUTE_LIST_EQ = TokenSet.create(CssTokens.EQ, CssTokens.DOL_EQ, CssTokens.MUL_EQ, CssTokens.OR_EQ, CssTokens.TILDE_EQ, CssTokens.XOR_EQ);
}
