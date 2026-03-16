/*
 * Copyright 2013-2026 consulo.io
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

package consulo.xstylesheet.table.mdn;

import consulo.xstylesheet.definition.XStyleSheetProperty;
import consulo.xstylesheet.definition.XStyleSheetTable;

import org.jspecify.annotations.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * An {@link XStyleSheetTable} backed by MDN CSS data.
 * Uses a {@link Map} for O(1) property lookup.
 *
 * @author VISTALL
 * @since 2026-03-15
 */
public record MdnXStyleSheetTable(Map<String, MdnXStyleSheetProperty> properties) implements XStyleSheetTable {
    @Nullable
    @Override
    public XStyleSheetProperty findProperty(String propertyName) {
        return properties.get(propertyName);
    }

    @Override
    public Collection<XStyleSheetProperty> getProperties() {
        return List.copyOf(properties.values());
    }
}
