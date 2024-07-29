/**
 * @author VISTALL
 * @since 11-Sep-22
 */
module consulo.xstylesheet {
    requires transitive consulo.css.xstylesheet.api;

    requires transitive consulo.language.api;
    requires transitive consulo.language.editor.api;

    exports consulo.xstylesheet;
    exports consulo.xstylesheet.codeInsight;
    exports consulo.xstylesheet.editor.lineMarker;
    exports consulo.xstylesheet.table;
}