/**
 * @author VISTALL
 * @since 2026-03-15
 */
module consulo.css.language.impl {
    requires consulo.language.api;
    requires consulo.language.impl;
    requires consulo.language.editor.api;
    requires consulo.language.code.style.api;

    requires consulo.css.xstylesheet.api;
    
    exports consulo.css.lang;
    exports consulo.css.lang.formatting;
    exports consulo.css.lang.lexer;
    exports consulo.css.lang.parser;
    exports consulo.css.lang.psi;
    exports consulo.css.localize;
}