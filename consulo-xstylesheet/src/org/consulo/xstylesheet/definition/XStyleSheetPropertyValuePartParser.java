package org.consulo.xstylesheet.definition;

import com.intellij.codeInsight.lookup.LookupElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public interface XStyleSheetPropertyValuePartParser {
  @Nullable
  Object fromString(@NotNull String stringValue, String value);

  @NotNull
  List<LookupElement> getLookupElements(String value);
}
