package org.consulo.xstylesheet.definition.value.impl;

import org.consulo.xstylesheet.definition.XStyleSheetPropertyValuePart;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class KeywordXStyleSheetValue implements XStyleSheetPropertyValuePart<String> {
    @Nullable
    @Override
    public String fromString(@NotNull String stringValue) {
        return stringValue;
    }
}
