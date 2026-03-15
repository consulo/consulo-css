/**
 * @author VISTALL
 * @since 2022-09-11
 */
module consulo.css.xstylesheet.api {
    requires transitive consulo.language.api;
    requires transitive consulo.language.editor.api;

    exports consulo.xstylesheet.definition;
    exports consulo.xstylesheet.definition.impl;
    exports consulo.xstylesheet.definition.value.impl;
    exports consulo.xstylesheet.highlight;
    exports consulo.xstylesheet.icon;
    exports consulo.xstylesheet.psi;
    exports consulo.xstylesheet.psi.reference;
    exports consulo.xstylesheet.psi.reference.nameResolving;
}