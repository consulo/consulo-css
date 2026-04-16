/**
 * @author VISTALL
 * @since 2022-09-11
 */
module consulo.css {
    requires consulo.css.xstylesheet.api;
    requires consulo.css.language.impl;
    requires consulo.language.editor.refactoring.api;
    requires consulo.language.code.style.ui.api;
    requires consulo.file.editor.api;
    requires consulo.configurable.api;

    requires com.intellij.xml;

    exports consulo.css.editor.completion;
    exports consulo.css.editor.structureView;
    exports consulo.css.html;
    exports consulo.css.html.lexer;
    exports consulo.css.html.psi;
}