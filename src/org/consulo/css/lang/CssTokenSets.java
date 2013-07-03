package org.consulo.css.lang;

import com.intellij.psi.tree.TokenSet;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public interface CssTokenSets {
  TokenSet WHITE_SPACES = TokenSet.create(CssTokens.WHITE_SPACE);
  TokenSet STRINGS = TokenSet.create(CssTokens.STRING);
  TokenSet COMMENTS = TokenSet.create(CssTokens.BLOCK_COMMENT);
}
