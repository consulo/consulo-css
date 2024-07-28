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
import consulo.language.psi.PsiUtilCore;
import consulo.language.util.IncorrectOperationException;
import consulo.xstylesheet.psi.PsiXStyleSheetSelectorAttribute;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author VISTALL
 * @since 11.07.13.
 */
public class CssSelectorAttribute extends CssElement implements PsiXStyleSheetSelectorAttribute {
    public CssSelectorAttribute(@Nonnull ASTNode node) {
        super(node);
    }

    @RequiredReadAction
    @Override
    public PsiElement getValue() {
        PsiElement asString = findChildByType(CssTokens.STRING);
        if (asString != null) {
            return asString;
        }

        PsiElement[] children = getChildren();
        if (children.length == 2 && PsiUtilCore.getElementType(children[1]) == CssTokens.IDENTIFIER) {
            return children[1];
        }
        return null;
    }

    @RequiredReadAction
    @Nullable
    @Override
    public PsiElement getNameIdentifier() {
        return getFirstChild();
    }

    @RequiredWriteAction
    @Override
    public PsiElement setName(@Nonnull String s) throws IncorrectOperationException {
        return null;
    }
}
