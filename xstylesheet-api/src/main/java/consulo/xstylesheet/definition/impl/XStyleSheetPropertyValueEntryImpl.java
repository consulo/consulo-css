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

import java.util.List;

import consulo.xstylesheet.definition.XStyleSheetPropertyValueEntry;
import consulo.xstylesheet.definition.XStyleSheetPropertyValuePart;

/**
 * @author VISTALL
 * @since 2013-07-03
 */
public class XStyleSheetPropertyValueEntryImpl implements XStyleSheetPropertyValueEntry {
    private XStyleSheetPropertyValuePart[] myParts;

    public XStyleSheetPropertyValueEntryImpl(List<XStyleSheetPropertyValuePart> parts) {
        myParts = parts.toArray(new XStyleSheetPropertyValuePart[parts.size()]);
    }

    @Override
    public XStyleSheetPropertyValuePart[] getParts() {
        return myParts;
    }
}
