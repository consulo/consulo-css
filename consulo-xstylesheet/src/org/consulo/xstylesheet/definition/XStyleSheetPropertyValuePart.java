package org.consulo.xstylesheet.definition;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public interface XStyleSheetPropertyValuePart {
  XStyleSheetPropertyValuePartParser getParser();

  String getValue();

  Object fromString(String value);
}
