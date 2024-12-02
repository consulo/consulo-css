/**
 * @author VISTALL
 * @since 2022-09-11
 */
module consulo.css {
    requires consulo.css.xstylesheet.api;
    requires consulo.ide.api;

    exports consulo.css.editor.completion;
    exports consulo.css.editor.structureView;
    exports consulo.css.html;
    exports consulo.css.html.lexer;
    exports consulo.css.html.psi;
    exports consulo.css.icon;
    exports consulo.css.lang;
    exports consulo.css.lang.formatting;
    exports consulo.css.lang.lexer;
    exports consulo.css.lang.parser;
    exports consulo.css.lang.psi;
}