package org.consulo.xstylesheet.definition.value.impl;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import org.consulo.xstylesheet.definition.XStyleSheetPropertyValuePartParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class KeywordXStyleSheetValue implements XStyleSheetPropertyValuePartParser, LikeKeywordXStyleSheetPropertyValuePartParser {
  @Nullable
  @Override
  public String fromString(@NotNull String stringValue, String value) {
    if (stringValue.equals(value)) {
      return stringValue;
    }
    return null;
  }

  @NotNull
  @Override
  public List<LookupElement> getLookupElements(String value) {
    LookupElementBuilder builder = LookupElementBuilder.create(value);
    builder = builder.withBoldness(true);
    return Collections.<LookupElement>singletonList(builder);
  }
}
