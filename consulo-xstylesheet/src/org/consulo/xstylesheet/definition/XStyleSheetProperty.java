package org.consulo.xstylesheet.definition;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public interface XStyleSheetProperty {
  String getName();

  XStyleSheetPropertyValueEntry[] getValidEntries();

  XStyleSheetPropertyValueEntry[] getInitialEntries();
}
