/**
 * @author VISTALL
 * @since 2026-03-16
 */
module consulo.scss.language.impl {
    requires consulo.language.api;
    requires consulo.language.impl;
    requires consulo.language.editor.api;
    requires consulo.language.code.style.api;

    requires consulo.css.xstylesheet.api;
    requires consulo.css.language.impl;

    exports consulo.scss.lang;
    exports consulo.scss.lang.lexer;
    exports consulo.scss.lang.parser;
    exports consulo.scss.lang.psi;
}
