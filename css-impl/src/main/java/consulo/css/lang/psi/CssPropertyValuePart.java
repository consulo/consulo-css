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

package consulo.css.lang.psi;

import consulo.annotation.access.RequiredReadAction;
import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiElement;
import consulo.util.collection.ArrayUtil;
import consulo.xstylesheet.definition.XStyleSheetProperty;
import consulo.xstylesheet.definition.XStyleSheetPropertyValueEntry;
import consulo.xstylesheet.definition.XStyleSheetPropertyValuePart;
import consulo.xstylesheet.definition.value.impl.VarFunctionCallValidator;
import consulo.xstylesheet.psi.PsiXStyleSheetProperty;
import consulo.xstylesheet.psi.PsiXStyleSheetPropertyValuePart;
import jakarta.annotation.Nullable;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * @author VISTALL
 * @since 2013-07-03
 */
public class CssPropertyValuePart extends CssElement implements PsiXStyleSheetPropertyValuePart {
    public CssPropertyValuePart(@Nonnull ASTNode node) {
        super(node);
    }

    @RequiredReadAction
    @Override
    public boolean isSoft() {
        PsiElement firstChild = getFirstChild();
        return firstChild instanceof CssFunctionCall functionCall && VarFunctionCallValidator.VAR_NAME.equals(functionCall.getCallName());
    }

    @RequiredReadAction
    @Override
    public Object getValue() {
        Map.Entry<XStyleSheetPropertyValuePart, Object> entry = find();
        return entry == null ? null : entry.getValue();
    }

    @Nullable
    @RequiredReadAction
    private Map.Entry<XStyleSheetPropertyValuePart, Object> find() {
        XStyleSheetPropertyValueEntry validEntry = findEntry();
        if (validEntry == null) {
            return null;
        }

        for (XStyleSheetPropertyValuePart valuePart : validEntry.getParts()) {
            Object o = valuePart.getNativeValue(this);
            if (o != null) {
                return Map.entry(valuePart, o);
            }
        }
        return null;
    }

    @Override
    public void setValue(@Nonnull Object value) {
        XStyleSheetPropertyValueEntry validEntry = findEntry();
        if (validEntry == null) {
            return;
        }

        for (XStyleSheetPropertyValuePart valuePart : validEntry.getParts()) {
            if (valuePart.setNativeValue(valuePart, value)) {
                break;
            }
        }
    }

    @Override
    public XStyleSheetPropertyValuePart getValuePart() {
        Map.Entry<XStyleSheetPropertyValuePart, Object> entry = find();
        return entry == null ? null : entry.getKey();
    }

    @Override
    public XStyleSheetPropertyValuePart[] getValueParts() {
        XStyleSheetPropertyValueEntry validEntry = findEntry();
        if (validEntry == null) {
            return XStyleSheetPropertyValuePart.EMPTY_ARRAY;
        }
        return validEntry.getParts();
    }

    @RequiredReadAction
    private XStyleSheetPropertyValueEntry findEntry() {
        PsiXStyleSheetProperty parent = (PsiXStyleSheetProperty)getParent();

        XStyleSheetProperty xStyleSheetProperty = parent.getXStyleSheetProperty();
        if (xStyleSheetProperty == null) {
            return null;
        }

        XStyleSheetPropertyValueEntry[] validEntries = xStyleSheetProperty.getValidEntries();

        PsiXStyleSheetPropertyValuePart[] parts = parent.getParts();
        if (parts.length == 0 || validEntries.length == 0) {
            return null;
        }

        int i = ArrayUtil.indexOf(parts, this);

        return i >= 0 && i < validEntries.length ? validEntries[i] : null;
    }
}
