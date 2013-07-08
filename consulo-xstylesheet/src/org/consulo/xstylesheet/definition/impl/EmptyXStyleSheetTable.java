package org.consulo.xstylesheet.definition.impl;

import org.consulo.xstylesheet.definition.XStyleSheetProperty;
import org.consulo.xstylesheet.definition.XStyleSheetTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class EmptyXStyleSheetTable implements XStyleSheetTable {
  public static final XStyleSheetTable INSTANCE = new EmptyXStyleSheetTable();
  @Nullable
  @Override
  public XStyleSheetProperty findProperty(@NotNull String propertyName) {
    return null;
  }

  @NotNull
  @Override
  public List<XStyleSheetProperty> getProperties() {
    return Collections.emptyList();
  }
}
