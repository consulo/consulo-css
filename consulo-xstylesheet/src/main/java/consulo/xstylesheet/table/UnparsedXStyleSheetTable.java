/*
 * Copyright 2013-2020 consulo.io
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

package consulo.xstylesheet.table;

import consulo.xstylesheet.definition.XStyleSheetProperty;
import consulo.xstylesheet.definition.XStyleSheetTable;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.*;

/**
 * @author VISTALL
 * @since 2020-08-08
 */
public class UnparsedXStyleSheetTable implements XStyleSheetTable {
    private final Map<String, XStyleSheetProperty> myNames = new HashMap<>();

    public UnparsedXStyleSheetTable(String[] names) {
        for (String name : names) {
            myNames.put(name, new UnparsedXStyleSheetProperty(name));
        }
    }

    @Nullable
    @Override
    public XStyleSheetProperty findProperty(@Nonnull String propertyName) {
        return myNames.get(propertyName);
    }

    @Nonnull
    @Override
    public Collection<XStyleSheetProperty> getProperties() {
        return myNames.values();
    }
}
