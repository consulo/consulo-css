package org.consulo.css.lang;

import com.intellij.lang.Language;

/**
 * @author VISTALL
 * @since 23:56/12.06.13
 */
public class CssLanguage extends Language {
    public static final CssLanguage INSTANCE = new CssLanguage();

    private CssLanguage() {
        super("CSS", "text/css");
    }
}
