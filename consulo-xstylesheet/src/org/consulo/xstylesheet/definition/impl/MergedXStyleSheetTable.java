package org.consulo.xstylesheet.definition.impl;

import org.consulo.xstylesheet.definition.XStyleSheetProperty;
import org.consulo.xstylesheet.definition.XStyleSheetTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class MergedXStyleSheetTable implements XStyleSheetTable {
    private final XStyleSheetTable[] myTables;

    public MergedXStyleSheetTable(XStyleSheetTable... myTables) {
        this.myTables = myTables;
    }

    @Nullable
    @Override
    public XStyleSheetProperty findProperty(@NotNull String propertyName) {
        for (XStyleSheetTable myTable : myTables) {
            XStyleSheetProperty property = myTable.findProperty(propertyName);
            if(property != null) {
                return property;
            }
        }
        return null;
    }
}
