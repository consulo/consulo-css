package org.consulo.css.lang.lexer;

import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.FlexLexer;

/**
 * @author VISTALL
 * @since 02.07.13.
 */
public class CssLexer extends FlexAdapter {
    public CssLexer() {
        super(new _CssLexer((java.io.Reader) null));
    }
}
