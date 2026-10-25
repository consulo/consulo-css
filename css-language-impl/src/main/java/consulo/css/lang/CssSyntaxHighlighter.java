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

import consulo.codeEditor.CodeInsightColors;
import consulo.colorScheme.TextAttributesKey;
import consulo.css.lang.lexer._CssLexer;
import consulo.language.ast.IElementType;
import consulo.language.editor.highlight.SyntaxHighlighterBase;
import consulo.language.lexer.Lexer;
import consulo.xstylesheet.highlight.XStyleSheetColors;

import java.util.HashMap;
import java.util.Map;


/**
 * @author VISTALL
 * @since 2013-07-03
 */
public class CssSyntaxHighlighter extends SyntaxHighlighterBase {
    public static void storeDefaults(Map<IElementType, TextAttributesKey> keys) {
        keys.put(CssTokens.NUMBER, XStyleSheetColors.NUMBER);
        keys.put(CssTokens.STRING, XStyleSheetColors.STRING);
        keys.put(CssTokens.BLOCK_COMMENT, XStyleSheetColors.BLOCK_COMMENT);
        keys.put(CssTokens.IMPORTANT_KEYWORD, XStyleSheetColors.KEYWORD);
        keys.put(CssTokens.CHARSET_KEYWORD, XStyleSheetColors.KEYWORD);
        keys.put(CssTokens.FONT_FACE_KEYWORD, XStyleSheetColors.KEYWORD);
        keys.put(CssTokens.IMPORT_KEYWORD, XStyleSheetColors.KEYWORD);
        keys.put(CssTokens.NAMESPACE_KEYWORD, XStyleSheetColors.KEYWORD);
        keys.put(CssTokens.AMPERSAND, XStyleSheetColors.KEYWORD);
        keys.put(CssTokens.AT_KEYWORD, XStyleSheetColors.KEYWORD);
        keys.put(CssTokens.MEDIA_KEYWORD, XStyleSheetColors.KEYWORD);
        keys.put(CssTokens.KEYFRAMES_KEYWORD, XStyleSheetColors.KEYWORD);
        keys.put(CssTokens.PAGE_KEYWORD, XStyleSheetColors.KEYWORD);
        keys.put(CssTokens.BAD_CHARACTER, CodeInsightColors.UNMATCHED_BRACE_ATTRIBUTES);
    }

    private final Map<IElementType, TextAttributesKey> myKeys = new HashMap<>();

    public CssSyntaxHighlighter() {
        storeDefaults(myKeys);
    }

    @Override
    public Lexer getHighlightingLexer() {
        return new _CssLexer();
    }

    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType elementType) {
        return pack(myKeys.get(elementType));
    }
}
