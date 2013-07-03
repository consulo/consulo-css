package org.consulo.xstylesheet.definition.value.impl;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.ui.ColorUtil;
import com.intellij.util.ui.ColorIcon;
import org.consulo.xstylesheet.definition.XStyleSheetPropertyValuePartParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class ColorXStyleSheetValue implements XStyleSheetPropertyValuePartParser {
  @Nullable
  @Override
  public Color fromString(@NotNull String stringValue, String value) {
    try {
      return ColorUtil.fromHex(stringValue);
    } catch (Exception e) {
      return null;
    }
  }

  @NotNull
  @Override
  public List<LookupElement> getLookupElements(String value) {
    return Collections.emptyList();
  }
}