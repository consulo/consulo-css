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

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.colors.CodeInsightColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import consulo.css.lang.lexer._CssLexer;
import consulo.xstylesheet.highlight.XStyleSheetColors;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class CssSyntaxHighlighter extends SyntaxHighlighterBase {
  private Map<IElementType, TextAttributesKey> myKeys = new HashMap<IElementType, TextAttributesKey>()
  {
    {
      put(CssTokens.NUMBER, XStyleSheetColors.NUMBER);
      put(CssTokens.STRING, XStyleSheetColors.STRING);
      put(CssTokens.BLOCK_COMMENT, XStyleSheetColors.BLOCK_COMMENT);
      put(CssTokens.BAD_CHARACTER, CodeInsightColors.UNMATCHED_BRACE_ATTRIBUTES);
    }
  };

  @NotNull
  @Override
  public Lexer getHighlightingLexer() {
    return new _CssLexer();
  }

  @NotNull
  @Override
  public TextAttributesKey[] getTokenHighlights(IElementType elementType) {
    return pack(myKeys.get(elementType));
  }
}
