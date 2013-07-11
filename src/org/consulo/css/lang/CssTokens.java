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
    IElementType STRING = new CssElementType("STRING");
    IElementType NUMBER = new CssElementType("NUMBER");
    IElementType SHARP = new CssElementType("SHARP");
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
}
