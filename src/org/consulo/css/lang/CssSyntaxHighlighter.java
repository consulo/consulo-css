package org.consulo.css.lang;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import org.consulo.css.lang.lexer.CssLexer;
import org.consulo.xstylesheet.highlight.XStyleSheetColors;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

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
    }
  };

  @NotNull
  @Override
  public Lexer getHighlightingLexer() {
    return new CssLexer();
  }

  @NotNull
  @Override
  public TextAttributesKey[] getTokenHighlights(IElementType elementType) {
    return pack(myKeys.get(elementType));
  }
}
