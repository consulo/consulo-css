package org.consulo.xstylesheet.definition.impl;

import org.consulo.xstylesheet.definition.XStyleSheetProperty;
import org.consulo.xstylesheet.definition.XStyleSheetTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class XStyleSheetTableImpl implements XStyleSheetTable {

  private List<XStyleSheetProperty> myProperties;

  public XStyleSheetTableImpl(List<XStyleSheetProperty> properties) {
    myProperties = properties;
  }

  @Nullable
  @Override
  public XStyleSheetProperty findProperty(@NotNull String propertyName) {
    for (XStyleSheetProperty property : myProperties) {
      if(property.getName().equals(propertyName)) {
        return property;
      }
    }
    return null;
  }
}
