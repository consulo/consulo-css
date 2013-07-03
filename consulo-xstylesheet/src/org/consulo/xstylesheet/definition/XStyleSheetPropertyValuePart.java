package org.consulo.xstylesheet.definition;

import com.intellij.codeInsight.lookup.LookupElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public interface XStyleSheetPropertyValuePart {
  XStyleSheetPropertyValuePartParser getParser();

  String getValue();

  Object fromString(String value);

  List<LookupElement> getLookupElements();
}
