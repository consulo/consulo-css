/*
 * Copyright 2013 must-be.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package consulo.xstylesheet.definition.impl;

import consulo.xstylesheet.definition.XStyleSheetProperty;
import consulo.xstylesheet.definition.XStyleSheetTable;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author VISTALL
 * @since 2013-07-03
 */
public class MergedXStyleSheetTable implements XStyleSheetTable {
    private final XStyleSheetTable[] myTables;

    public MergedXStyleSheetTable(XStyleSheetTable... tables) {
        myTables = tables;
    }

    @Nullable
    @Override
    public XStyleSheetProperty findProperty(@Nonnull String propertyName) {
        for (XStyleSheetTable myTable : myTables) {
            XStyleSheetProperty property = myTable.findProperty(propertyName);
            if (property != null) {
                return property;
            }
        }
        return null;
    }

    @Nonnull
    @Override
    public Collection<XStyleSheetProperty> getProperties() {
        List<XStyleSheetProperty> list = new ArrayList<>();
        for (XStyleSheetTable table : myTables) {
            list.addAll(table.getProperties());
        }
        return list;
    }
}
