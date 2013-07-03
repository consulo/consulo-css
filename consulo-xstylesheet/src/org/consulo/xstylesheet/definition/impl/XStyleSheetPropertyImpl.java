package org.consulo.xstylesheet.definition.impl;

import org.consulo.xstylesheet.definition.XStyleSheetProperty;
import org.consulo.xstylesheet.definition.XStyleSheetPropertyValueEntry;
import org.consulo.xstylesheet.definition.XStyleSheetPropertyValuePart;

import java.util.List;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class XStyleSheetPropertyImpl implements XStyleSheetProperty {
  private String myName;
  private XStyleSheetPropertyValueEntry[] myValidEntries;
  private XStyleSheetPropertyValueEntry[] myInitialEntries;

  public XStyleSheetPropertyImpl(String name, List<XStyleSheetPropertyValueEntry> validEntries, List<XStyleSheetPropertyValueEntry> initialEntries) {
    myName = name;
    myValidEntries = validEntries.toArray(new XStyleSheetPropertyValueEntry[validEntries.size()]);
    myInitialEntries = initialEntries.toArray(new XStyleSheetPropertyValueEntry[initialEntries.size()]);
  }

  @Override
  public String getName() {
    return myName;
  }

  @Override
  public XStyleSheetPropertyValueEntry[] getValidEntries() {
    return myValidEntries;
  }

  @Override
  public XStyleSheetPropertyValueEntry[] getInitialEntries() {
    return myInitialEntries;
  }
}
