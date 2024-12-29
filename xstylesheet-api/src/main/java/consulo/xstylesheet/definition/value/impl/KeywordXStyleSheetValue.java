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

import consulo.language.editor.completion.lookup.LookupElement;
import consulo.language.editor.completion.lookup.LookupElementBuilder;
import consulo.language.editor.rawHighlight.HighlightInfo;
import consulo.language.editor.rawHighlight.HighlightInfoType;
import consulo.xstylesheet.highlight.XStyleSheetColors;
import consulo.xstylesheet.psi.PsiXStyleSheetPropertyValuePart;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * @author VISTALL
 * @since 2013-07-03
 */
public class KeywordXStyleSheetValue extends TextBasedXStyleSheetPropertyValuePartParser {
    @Nullable
    @Override
    public String fromString(@Nonnull String stringValue, String value) {
        if (stringValue.equals(value)) {
            return stringValue;
        }
        return null;
    }

    @Nonnull
    @Override
    public List<HighlightInfo> createHighlights(@Nonnull PsiXStyleSheetPropertyValuePart valuePart) {
        HighlightInfo.Builder builder = HighlightInfo.newHighlightInfo(HighlightInfoType.INFORMATION);
        builder.textAttributes(XStyleSheetColors.KEYWORD);
        builder.range(valuePart);
        return Collections.singletonList(builder.create());
    }

    @Nonnull
    @Override
    public List<LookupElement> getLookupElements(String value) {
        LookupElementBuilder builder = LookupElementBuilder.create(value);
        builder = builder.withBoldness(true);
        return Collections.<LookupElement>singletonList(builder);
    }
}
