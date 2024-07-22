/**
 * @author VISTALL
 * @since 11-Sep-22
 */
module consulo.css.emmet
{
    requires consulo.ide.api;
    // TODO remove in future
    requires java.desktop;

    requires consulo.emmet;

    requires com.intellij.xml;

    exports com.intellij.application.options.emmet.css;

    opens com.intellij.application.options.emmet.css to consulo.util.xml.serializer;
}