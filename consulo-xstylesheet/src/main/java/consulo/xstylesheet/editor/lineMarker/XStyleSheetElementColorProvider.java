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

package consulo.xstylesheet.editor.lineMarker;

import consulo.annotation.access.RequiredReadAction;
import consulo.annotation.access.RequiredWriteAction;
import consulo.annotation.component.ExtensionImpl;
import consulo.application.WriteAction;
import consulo.language.psi.ElementColorProvider;
import consulo.language.psi.PsiElement;
import consulo.ui.color.ColorValue;
import consulo.xstylesheet.psi.PsiXStyleSheetPropertyValuePart;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * @author VISTALL
 * @since 2013-07-03
 */
@ExtensionImpl
public class XStyleSheetElementColorProvider implements ElementColorProvider {
    @RequiredReadAction
    @Nullable
    @Override
    public ColorValue getColorFrom(@Nonnull PsiElement psiElement) {
        if (psiElement instanceof PsiXStyleSheetPropertyValuePart xStyleSheetPropertyValuePart) {
            Object value = null;
            try {
                value = xStyleSheetPropertyValuePart.getValue();
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
    public void setColorTo(@Nonnull PsiElement psiElement, @Nonnull ColorValue color) {
        if (psiElement instanceof PsiXStyleSheetPropertyValuePart xStyleSheetPropertyValuePart) {
            WriteAction.run(() -> xStyleSheetPropertyValuePart.setValue(color));
        }
    }
}
