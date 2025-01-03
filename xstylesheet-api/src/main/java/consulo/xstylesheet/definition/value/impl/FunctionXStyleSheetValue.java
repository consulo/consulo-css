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

import consulo.annotation.access.RequiredReadAction;
import consulo.language.editor.completion.lookup.LookupElement;
import consulo.language.editor.rawHighlight.HighlightInfo;
import consulo.language.psi.PsiElement;
import consulo.xstylesheet.definition.XStyleSheetFunctionCallDescriptor;
import consulo.xstylesheet.psi.PsiXStyleSheetFunctionCall;
import consulo.xstylesheet.psi.PsiXStyleSheetPropertyValuePart;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * @author VISTALL
 * @since 2013-10-09
 */
public class FunctionXStyleSheetValue extends BaseXStyleSheetPropertyValuePartParser {
    @Nonnull
    @Override
    public List<HighlightInfo> createHighlights(@Nonnull PsiXStyleSheetPropertyValuePart valuePart) {
        Object nativeValue = getNativeValue(valuePart, null);
        if (nativeValue instanceof XStyleSheetFunctionCallDescriptor functionCallDescriptor) {
            return functionCallDescriptor.createHighlights((PsiXStyleSheetFunctionCall)valuePart.getFirstChild());
        }
        return Collections.emptyList();
    }

    @Nullable
    @Override
    @RequiredReadAction
    public Object getNativeValue(@Nonnull PsiXStyleSheetPropertyValuePart valuePart, String value) {
        PsiElement firstChild = valuePart.getFirstChild();
        if (firstChild instanceof PsiXStyleSheetFunctionCall functionCall) {
            List<XStyleSheetFunctionCallDescriptor> extensionList = valuePart.getProject()
                .getApplication()
                .getExtensionList(XStyleSheetFunctionCallDescriptor.class);
            for (XStyleSheetFunctionCallDescriptor descriptor : extensionList) {
                if (descriptor.isMyFunction(functionCall)) {
                    return descriptor;
                }
            }
        }
        return null;
    }

    @Nonnull
    @Override
    public List<LookupElement> getLookupElements(String value) {
        return Collections.emptyList();
    }
}
