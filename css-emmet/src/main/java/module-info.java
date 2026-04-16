/**
 * @author VISTALL
 * @since 2022-09-11
 */
module consulo.css.emmet
{
    requires consulo.language.editor.refactoring.api;
    requires consulo.configurable.api;
    // TODO remove in future
    requires java.desktop;

    requires consulo.emmet;

    requires com.intellij.xml;

    exports com.intellij.application.options.emmet.css;

    opens com.intellij.application.options.emmet.css to consulo.util.xml.serializer;
}