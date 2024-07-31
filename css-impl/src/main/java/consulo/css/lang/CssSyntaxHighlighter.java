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
import consulo.util.collection.MultiMap;
import consulo.xstylesheet.highlight.XStyleSheetColors;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 2013-07-03
 */
public class CssSyntaxHighlighter extends SyntaxHighlighterBase {
    public static void storeDefaults(MultiMap<IElementType, TextAttributesKey> keys) {
        keys.putValue(CssTokens.NUMBER, XStyleSheetColors.NUMBER);
        keys.putValue(CssTokens.STRING, XStyleSheetColors.STRING);
        keys.putValue(CssTokens.BLOCK_COMMENT, XStyleSheetColors.BLOCK_COMMENT);
        keys.putValue(CssTokens.IMPORTANT_KEYWORD, XStyleSheetColors.KEYWORD);
        keys.putValue(CssTokens.CHARSET_KEYWORD, XStyleSheetColors.KEYWORD);
        keys.putValue(CssTokens.FONT_FACE_KEYWORD, XStyleSheetColors.KEYWORD);
        keys.putValue(CssTokens.IMPORT_KEYWORD, XStyleSheetColors.KEYWORD);
        keys.putValue(CssTokens.FONT_FACE_KEYWORD, XStyleSheetColors.KEYWORD);
        keys.putValue(CssTokens.NAMESPACE_KEYWORD, XStyleSheetColors.KEYWORD);
        keys.putValue(CssTokens.BAD_CHARACTER, CodeInsightColors.UNMATCHED_BRACE_ATTRIBUTES);
    }

    private final MultiMap<IElementType, TextAttributesKey> myKeys = MultiMap.createLinked();

    public CssSyntaxHighlighter() {
        storeDefaults(myKeys);
    }

    @Nonnull
    @Override
    public Lexer getHighlightingLexer() {
        return new _CssLexer();
    }

    @Nonnull
    @Override
    public TextAttributesKey[] getTokenHighlights(@Nonnull IElementType elementType) {
        return myKeys.get(elementType).toArray(new TextAttributesKey[0]);
    }
}
