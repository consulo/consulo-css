package org.consulo.css;

import javax.swing.Icon;

import com.intellij.openapi.util.IconLoader;

/**
 * @author VISTALL
 * @since 17:57 25.06.13
 */
public interface CssIcons {
    Icon CssFile = IconLoader.findIcon("/org/consulo/css/icons/cssFile.png");
    Icon AtRule = IconLoader.findIcon("/org/consulo/css/icons/atrule.png");
    Icon CssClas = IconLoader.findIcon("/org/consulo/css/icons/css_class.png");
    Icon HtmlId = IconLoader.findIcon("/org/consulo/css/icons/html_id.png");
    Icon Property = IconLoader.findIcon("/org/consulo/css/icons/property.png");
    Icon PseudoElement = IconLoader.findIcon("/org/consulo/css/icons/pseudo-element.png");
}
