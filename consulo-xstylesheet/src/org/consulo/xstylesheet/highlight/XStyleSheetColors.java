package org.consulo.xstylesheet.highlight;

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public interface XStyleSheetColors {
  TextAttributesKey KEYWORD = TextAttributesKey.createTextAttributesKey("XSTYLESHEET_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
  TextAttributesKey PROPERTY_NAME = TextAttributesKey.createTextAttributesKey("XSTYLESHEET_PROPERTY_NAME", DefaultLanguageHighlighterColors.MARKUP_ATTRIBUTE);
}
