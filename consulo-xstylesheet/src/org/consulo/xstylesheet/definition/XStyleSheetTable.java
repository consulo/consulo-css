package org.consulo.xstylesheet.definition;

import org.consulo.annotations.Immutable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public interface XStyleSheetTable {
  @Nullable
  XStyleSheetProperty findProperty(@NotNull String propertyName);

  @NotNull
  @Immutable
  List<XStyleSheetProperty> getProperties();
}
