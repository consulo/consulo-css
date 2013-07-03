package org.consulo.xstylesheet.definition.impl;

import org.consulo.xstylesheet.definition.XStyleSheetPropertyValueEntry;
import org.consulo.xstylesheet.definition.XStyleSheetPropertyValuePart;

import java.util.List;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class XStyleSheetPropertyValueEntryImpl implements XStyleSheetPropertyValueEntry {
  private XStyleSheetPropertyValuePart[] myParts;

  public XStyleSheetPropertyValueEntryImpl(List<XStyleSheetPropertyValuePart> parts) {
    myParts = parts.toArray(new XStyleSheetPropertyValuePart[parts.size()]);
  }

  @Override
  public XStyleSheetPropertyValuePart[] getParts() {
    return myParts;
  }
}
