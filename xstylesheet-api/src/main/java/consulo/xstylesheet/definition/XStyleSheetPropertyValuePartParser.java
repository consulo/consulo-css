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

package consulo.xstylesheet.definition;

import consulo.language.editor.completion.lookup.LookupElement;
import consulo.language.editor.rawHighlight.HighlightInfo;
import consulo.xstylesheet.psi.PsiXStyleSheetPropertyValuePart;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.List;

/**
 * @author VISTALL
 * @since 2013-07-03
 */
public interface XStyleSheetPropertyValuePartParser {
    @Nonnull
    default List<HighlightInfo> createHighlights(@Nonnull PsiXStyleSheetPropertyValuePart valuePart) {
        return List.of();
    }

    @Nullable
    default Object getNativeValue(@Nonnull PsiXStyleSheetPropertyValuePart valuePart, String value) {
        return null;
    }

    @Nonnull
    default List<LookupElement> getLookupElements(String value) {
        return List.of();
    }
}
