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
import consulo.css.lang.CssTokens;
import consulo.document.util.TextRange;
import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiReference;
import consulo.language.psi.PsiReferenceBase;
import consulo.xstylesheet.psi.PsiXStyleSheetFunctionCall;
import consulo.xstylesheet.psi.PsiXStyleSheetFunctionCallParameterList;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * @author VISTALL
 * @since 2013-10-08
 */
public class CssFunctionCall extends CssElement implements PsiXStyleSheetFunctionCall {
    private static class Ref extends PsiReferenceBase<PsiElement> {
        @RequiredReadAction
        public Ref(@Nonnull CssFunctionCall element) {
            super(element, new TextRange(0, element.getCallElement().getTextLength()));
        }

        @RequiredReadAction
        @Nullable
        @Override
        public PsiElement resolve() {
            return new BuildInSymbolElementImpl(getElement());
        }
    }

    public CssFunctionCall(@Nonnull ASTNode node) {
        super(node);
    }

    @Override
    @RequiredReadAction
    public PsiReference getReference() {
        return new Ref(this);
    }

    @Nonnull
    @Override
    @RequiredReadAction
    public PsiElement getCallElement() {
        return findNotNullChildByType(CssTokens.IDENTIFIER);
    }

    @Nonnull
    @RequiredReadAction
    @Override
    public String getCallName() {
        PsiElement nameIdentifier = getCallElement();

        return nameIdentifier.getText();
    }

    @RequiredReadAction
    @Override
    public PsiXStyleSheetFunctionCallParameterList getParameterList() {
        return findChildByClass(PsiXStyleSheetFunctionCallParameterList.class);
    }

    @RequiredReadAction
    @Override
    public PsiElement[] getParameters() {
        PsiXStyleSheetFunctionCallParameterList parameterList = getParameterList();
        return parameterList == null ? PsiElement.EMPTY_ARRAY : parameterList.getParameters();
    }
}
