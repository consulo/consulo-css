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
import consulo.annotation.access.RequiredWriteAction;
import consulo.css.lang.CssTokens;
import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiNameIdentifierOwner;
import consulo.language.util.IncorrectOperationException;
import consulo.xstylesheet.definition.XStyleSheetProperty;
import consulo.xstylesheet.definition.XStyleSheetTable;
import consulo.xstylesheet.psi.PsiXStyleSheetProperty;
import consulo.xstylesheet.psi.PsiXStyleSheetPropertyValuePart;
import consulo.xstylesheet.psi.XStyleSheetFile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class CssProperty extends CssElement implements PsiNameIdentifierOwner, PsiXStyleSheetProperty {
    public static final CssProperty[] EMPTY_ARRAY = new CssProperty[0];

    public CssProperty(@Nonnull ASTNode node) {
        super(node);
    }

    @RequiredReadAction
    @Nullable
    @Override
    public PsiElement getNameIdentifier() {
        return findChildByType(CssTokens.IDENTIFIER);
    }

    @RequiredReadAction
    @Nullable
    @Override
    public XStyleSheetProperty getXStyleSheetProperty() {
        String name = getName();
        if (name == null) {
            return null;
        }
        PsiFile containingFile = getContainingFile();
        XStyleSheetTable xStyleSheetTable = XStyleSheetFile.getXStyleSheetTable(containingFile);
        return xStyleSheetTable.findProperty(name);
    }

    @Nonnull
    @Override
    public PsiXStyleSheetPropertyValuePart[] getParts() {
        return findChildrenByClass(PsiXStyleSheetPropertyValuePart.class);
    }

    @RequiredReadAction
    @Override
    public String getName() {
        PsiElement nameIdentifier = getNameIdentifier();
        return nameIdentifier == null ? null : nameIdentifier.getText();
    }

    @RequiredReadAction
    @Override
    public boolean isImportant() {
        return findChildByType(CssTokens.IMPORTANT_KEYWORD) != null;
    }

    @RequiredWriteAction
    @Override
    public PsiElement setName(@Nonnull String s) throws IncorrectOperationException {
        return null;
    }
}
