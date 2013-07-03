package org.consulo.xstylesheet.definition;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public interface XStyleSheetPropertyValuePart <T> {
    @Nullable
    T fromString(@NotNull String stringValue);
}
