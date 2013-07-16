package org.consulo.xstylesheet.highlight;

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public interface XStyleSheetColors {
	TextAttributesKey NUMBER = TextAttributesKey.createTextAttributesKey("XSTYLESHEET_NUMBER", DefaultLanguageHighlighterColors.NUMBER);

	TextAttributesKey STRING = TextAttributesKey.createTextAttributesKey("XSTYLESHEET_STRING", DefaultLanguageHighlighterColors.STRING);

	TextAttributesKey BLOCK_COMMENT = TextAttributesKey.createTextAttributesKey("XSTYLESHEET_BLOCK_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT);

	TextAttributesKey KEYWORD = TextAttributesKey.createTextAttributesKey("XSTYLESHEET_KEYWORD", DefaultLanguageHighlighterColors.STRING);

	TextAttributesKey SELECTOR_ELEMENT_NAME = TextAttributesKey.createTextAttributesKey("XSTYLESHEET_SELECTOR_ELEMENT_NAME", DefaultLanguageHighlighterColors.KEYWORD);

	TextAttributesKey SELECTOR_CLASS_NAME = TextAttributesKey.createTextAttributesKey("XSTYLESHEET_SELECTOR_CLASS_NAME", DefaultLanguageHighlighterColors.KEYWORD);

	TextAttributesKey SELECTOR_ID_NAME = TextAttributesKey.createTextAttributesKey("XSTYLESHEET_SELECTOR_ID_NAME", DefaultLanguageHighlighterColors.KEYWORD);

	TextAttributesKey ATTRIBUTE_NAME = TextAttributesKey.createTextAttributesKey("XSTYLESHEET_ATTRIBUTE_NAME", DefaultLanguageHighlighterColors.MARKUP_ATTRIBUTE);

	TextAttributesKey PROPERTY_NAME = TextAttributesKey.createTextAttributesKey("XSTYLESHEET_PROPERTY_NAME", DefaultLanguageHighlighterColors.MARKUP_ATTRIBUTE);

	TextAttributesKey PSEUDO_NAME = TextAttributesKey.createTextAttributesKey("XSTYLESHEET_PSEUDO_NAME", DefaultLanguageHighlighterColors.MARKUP_ATTRIBUTE);
}
