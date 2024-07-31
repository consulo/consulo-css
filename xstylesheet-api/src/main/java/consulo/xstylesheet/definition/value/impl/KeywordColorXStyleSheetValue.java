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
import consulo.ui.color.ColorValue;
import consulo.ui.image.ImageEffects;
import consulo.ui.util.ColorValueUtil;
import consulo.util.lang.StringUtil;
import consulo.xstylesheet.definition.XStyleSheetColor;
import consulo.xstylesheet.highlight.XStyleSheetColors;
import consulo.xstylesheet.psi.PsiXStyleSheetPropertyValuePart;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author VISTALL
 * @since 2013-07-03
 */
public class KeywordColorXStyleSheetValue extends TextBasedXStyleSheetPropertyValuePartParser {
    @Nonnull
    @Override
    public List<HighlightInfo> createHighlights(@Nonnull PsiXStyleSheetPropertyValuePart valuePart) {
        HighlightInfo.Builder builder = HighlightInfo.newHighlightInfo(HighlightInfoType.INFORMATION);
        builder.textAttributes(XStyleSheetColors.KEYWORD);
        builder.range(valuePart);
        return Collections.singletonList(builder.create());
    }

    @Nullable
    @Override
    public ColorValue fromString(@Nonnull String stringValue, String value) {
        XStyleSheetColor xStyleSheetColor = XStyleSheetColor.getColor(StringUtil.toLowerCase(stringValue));
        if (xStyleSheetColor == null) {
            return null;
        }

        try {
            return ColorValueUtil.fromHex("#" + xStyleSheetColor.colorCode());
        }
        catch (Exception e) {
            return null;
        }
    }

    @Nonnull
    @Override
    public List<LookupElement> getLookupElements(String value) {
        List<LookupElement> list = new ArrayList<>();
        for (XStyleSheetColor entry : XStyleSheetColor.values()) {
            LookupElementBuilder builder = LookupElementBuilder.create(entry.name());
            String hex = "#" + entry.colorCode();
            builder = builder.withIcon(ImageEffects.colorFilled(16, 16, ColorValueUtil.fromHex(hex)));
            builder = builder.withTypeText(hex, true);
            list.add(builder);
        }
        return list;
    }
}