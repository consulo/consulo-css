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

package consulo.xstylesheet.definition.value.impl;

import consulo.xstylesheet.definition.XStyleSheetPropertyValuePartParser;
import consulo.xstylesheet.psi.PsiXStyleSheetPropertyValuePart;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * @author VISTALL
 * @since 2013-10-09
 */
public abstract class TextBasedXStyleSheetPropertyValuePartParser implements XStyleSheetPropertyValuePartParser {
    @Nullable
    @Override
    public Object getNativeValue(@Nonnull PsiXStyleSheetPropertyValuePart valuePart, String value) {
        return fromString(valuePart.getText().trim(), value);
    }

    public abstract Object fromString(@Nonnull String valuePart, String value);
}
