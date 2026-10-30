/*
 * Copyright 2013-2014 must-be.org
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

package consulo.xstylesheet.highlight;

import consulo.language.psi.util.PsiTreeUtil;
import consulo.annotation.access.RequiredReadAction;
import consulo.annotation.access.RequiredWriteAction;
import consulo.language.psi.ElementColorProvider;
import consulo.language.psi.PsiElement;
import consulo.ui.color.ColorValue;
import consulo.xstylesheet.psi.PsiXStyleSheetPropertyValuePart;
import org.jspecify.annotations.Nullable;

/**
 * @author VISTALL
 * @since 2013-07-03
 */
public abstract class XStyleSheetElementColorProvider implements ElementColorProvider {
    @RequiredReadAction
    @Nullable
    @Override
    public ColorValue getColorFrom(PsiElement psiElement) {
        PsiXStyleSheetPropertyValuePart part = findValuePart(psiElement);
        if (part != null) {
            Object value = null;
            try {
                value = part.getValue();
            }
            catch (Exception ignored) {
            }

            if (value instanceof ColorValue colorValue) {
                return colorValue;
            }
        }
        return null;
    }

    @RequiredWriteAction
    @Override
    public void setColorTo(PsiElement psiElement, ColorValue color) {
        PsiXStyleSheetPropertyValuePart part = findValuePart(psiElement);
        if (part != null) {
            part.setValue(color);
        }
    }

    /**
     * The value part a leaf starts ({@code #bbb}, {@code red}, the {@code rgb} of {@code rgb(1, 2, 3)}): one gutter icon
     * per value, on its first leaf.
     */
    @Nullable
    @RequiredReadAction
    private static PsiXStyleSheetPropertyValuePart findValuePart(PsiElement leaf) {
        if (leaf.getFirstChild() != null) {
            return null;
        }
        PsiXStyleSheetPropertyValuePart part = PsiTreeUtil.getParentOfType(leaf, PsiXStyleSheetPropertyValuePart.class);
        return part != null && PsiTreeUtil.getDeepestFirst(part) == leaf ? part : null;
    }
}
