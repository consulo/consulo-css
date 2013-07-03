package org.consulo.xstylesheet.definition.value.impl;

import org.consulo.xstylesheet.definition.XStyleSheetPropertyValuePartParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class KeywordXStyleSheetValue implements XStyleSheetPropertyValuePartParser {
  @Nullable
  @Override
  public String fromString(@NotNull String stringValue, String value) {
    if (stringValue.equals(value)) {
      return stringValue;
    }
    return null;
  }
}
