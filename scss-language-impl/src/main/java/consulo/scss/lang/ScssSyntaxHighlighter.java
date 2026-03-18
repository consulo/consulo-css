/*
 * Copyright 2013-2026 consulo.io
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

package consulo.scss.lang;

import consulo.colorScheme.TextAttributesKey;
import consulo.css.lang.CssSyntaxHighlighter;
import consulo.language.ast.IElementType;
import consulo.language.editor.highlight.SyntaxHighlighterBase;
import consulo.language.lexer.Lexer;
import consulo.scss.lang.lexer._ScssLexer;
import consulo.xstylesheet.highlight.XStyleSheetColors;

import java.util.HashMap;
import java.util.Map;

/**
 * @author VISTALL
 * @since 2026-03-16
 */
public class ScssSyntaxHighlighter extends SyntaxHighlighterBase {
    private final Map<IElementType, TextAttributesKey> myKeys = new HashMap<>();

    public ScssSyntaxHighlighter() {
        CssSyntaxHighlighter.storeDefaults(myKeys);

        myKeys.put(ScssTokens.LINE_COMMENT, XStyleSheetColors.LINE_COMMENT);
        myKeys.put(ScssTokens.DOLLAR, XStyleSheetColors.KEYWORD);
        myKeys.put(ScssTokens.AMPERSAND, XStyleSheetColors.KEYWORD);
        myKeys.put(ScssTokens.MIXIN_KEYWORD, XStyleSheetColors.KEYWORD);
        myKeys.put(ScssTokens.INCLUDE_KEYWORD, XStyleSheetColors.KEYWORD);
        myKeys.put(ScssTokens.USE_KEYWORD, XStyleSheetColors.KEYWORD);
        myKeys.put(ScssTokens.FORWARD_KEYWORD, XStyleSheetColors.KEYWORD);
    }

    @Override
    public Lexer getHighlightingLexer() {
        return new _ScssLexer();
    }

    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType elementType) {
        return pack(myKeys.get(elementType));
    }
}
