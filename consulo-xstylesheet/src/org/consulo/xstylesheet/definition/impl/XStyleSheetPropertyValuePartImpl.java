package org.consulo.xstylesheet.definition.impl;

import org.consulo.xstylesheet.definition.XStyleSheetPropertyValuePart;
import org.consulo.xstylesheet.definition.XStyleSheetPropertyValuePartParser;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class XStyleSheetPropertyValuePartImpl implements XStyleSheetPropertyValuePart {
  private final XStyleSheetPropertyValuePartParser myParser;
  private final String myValue;

  public XStyleSheetPropertyValuePartImpl(XStyleSheetPropertyValuePartParser parser, String value) {
    myParser = parser;
    myValue = value;
  }

  @Override
  public XStyleSheetPropertyValuePartParser getParser() {
    return myParser;
  }

  @Override
  public String getValue() {
    return myValue;
  }

  @Override
  public Object fromString(String value) {
    return myParser.fromString(value, myValue);
  }
}
