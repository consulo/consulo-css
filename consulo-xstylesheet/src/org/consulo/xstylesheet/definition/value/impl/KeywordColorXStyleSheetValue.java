package org.consulo.xstylesheet.definition.value.impl;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.ui.ColorUtil;
import com.intellij.util.ui.ColorIcon;
import org.consulo.xstylesheet.definition.XStyleSheetPropertyValuePartParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class KeywordColorXStyleSheetValue implements XStyleSheetPropertyValuePartParser, LikeXStyleSheetPropertyValuePartParser {
  private Map<String, String> myDefaultColors = new HashMap<String, String>() {
    {
      put("maroon", "#800000");
      put("red", "#ff0000");
      put("orange", "#ffA500");
      put("yellow", "#ffff00");
      put("olive", "#808000");
      put("purple", "#800080");
      put("fuchsia", "#ff00ff");
      put("white", "#ffffff");
      put("lime", "#00ff00");
      put("green", "#008000");
      put("navy", "#000080");
      put("blue", "#0000f0");
      put("faqua", "#00ffff");
      put("teal", "#008080");
      put("black", "#000000");
      put("silver", "#c0c0c0");
      put("gray", "#808080");
    }
  };

  @Nullable
  @Override
  public Color fromString(@NotNull String stringValue, String value) {
    stringValue = stringValue.toLowerCase();
    stringValue = myDefaultColors.get(stringValue);
    if (stringValue == null) {
      return null;
    }

    try {
      return ColorUtil.fromHex(stringValue);
    } catch (Exception e) {
      return null;
    }
  }

  @NotNull
  @Override
  public List<LookupElement> getLookupElements(String value) {
    List<LookupElement> list = new ArrayList<LookupElement>();
    for (Map.Entry<String, String> entry : myDefaultColors.entrySet()) {
      LookupElementBuilder builder = LookupElementBuilder.create(entry.getKey());
      builder = builder.withIcon(new ColorIcon(12, ColorUtil.fromHex(entry.getValue())));
      builder = builder.withTypeText(entry.getValue(), true);
      list.add(builder);
    }
    return list;
  }
}