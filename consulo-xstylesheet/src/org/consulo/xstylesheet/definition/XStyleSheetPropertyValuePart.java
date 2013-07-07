package org.consulo.xstylesheet.definition;

import com.intellij.codeInsight.lookup.LookupElement;

import java.util.List;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public interface XStyleSheetPropertyValuePart {
  XStyleSheetPropertyValuePart[] EMPTY_ARRAY = new XStyleSheetPropertyValuePart[0];

  XStyleSheetPropertyValuePartParser getParser();

  String getValue();

  Object fromString(String value);

  List<LookupElement> getLookupElements();
}
