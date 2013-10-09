package org.consulo.xstylesheet.definition.value.impl;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;

/**
 * @author VISTALL
 * @since 08.07.13.
 */
public class BaseNumberXStyleSheetValue extends TextBasedXStyleSheetPropertyValuePartParser {
  private String[] myPrefixes;

  public BaseNumberXStyleSheetValue(@NotNull String... prefixes) {
    myPrefixes = prefixes;
  }

  @Nullable
  @Override
  public Object fromString(@NotNull String stringValue, String value) {
    if(value != null && !stringValue.equals(value)) {
      return null;
    }

    String prefix = null;
    for(String t : myPrefixes) {
      if(stringValue.endsWith(t)) {
        prefix = t;
        break;
      }
    }

    if(prefix == null && myPrefixes.length > 0) {
      return null;
    }
    else if(prefix != null) {
      stringValue = stringValue.substring(0, stringValue.length() - prefix.length());
    }

    try {
      return Integer.parseInt(stringValue);
    } catch (NumberFormatException e) {
      //
    }
    return null;
  }

  @NotNull
  @Override
  public List<LookupElement> getLookupElements(String value) {
    LookupElementBuilder builder = LookupElementBuilder.create(value);
    return Collections.<LookupElement>singletonList(builder);
  }
}
