package org.consulo.xstylesheet.definition.value.impl;

import java.awt.Color;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.ui.ColorUtil;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class ColorXStyleSheetValue extends TextBasedXStyleSheetPropertyValuePartParser {
  @Nullable
  @Override
  public Color fromString(@NotNull String stringValue, String value) {
    try {
      return ColorUtil.fromHex(stringValue);
    } catch (Exception e) {
      return null;
    }
  }
}