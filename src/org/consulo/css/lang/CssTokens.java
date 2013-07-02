package org.consulo.css.lang;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;

/**
 * @author VISTALL
 * @since 02.07.13
 */
public interface CssTokens extends TokenType {
    IElementType BLOCK_COMMENT = new CssElementType("BLOCK_COMMENT");

    IElementType IDENTIFIER = new CssElementType("IDENTIFIER");
    IElementType SHARP = new CssElementType("SHARP");
    IElementType DOT = new CssElementType("DOT");
    IElementType LBRACE = new CssElementType("LBRACE");
    IElementType RBRACE = new CssElementType("RBRACE");
}
