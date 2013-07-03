package org.consulo.xstylesheet.definition;

import org.consulo.annotations.Immutable;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public interface XStyleSheetPropertyValue {
    @NotNull
    @Immutable
    XStyleSheetPropertyValuePart[] getParts();
}
