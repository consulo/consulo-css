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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

/**
 * @author VISTALL
 * @since 2013-07-03
 */
public class XStyleSheetTableImpl implements XStyleSheetTable {
    private List<XStyleSheetProperty> myProperties;

    public XStyleSheetTableImpl(List<XStyleSheetProperty> properties) {
        myProperties = properties;
    }

    @Nullable
    @Override
    public XStyleSheetProperty findProperty(@Nonnull String propertyName) {
        for (XStyleSheetProperty property : myProperties) {
            if (property.getName().equals(propertyName)) {
                return property;
            }
        }
        return null;
    }

    @Nonnull
    @Override
    public Collection<XStyleSheetProperty> getProperties() {
        return myProperties;
    }
}
