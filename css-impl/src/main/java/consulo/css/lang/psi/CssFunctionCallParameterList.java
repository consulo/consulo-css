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
import consulo.language.ast.ASTNode;
import consulo.language.ast.TokenSet;
import consulo.language.psi.PsiElement;
import consulo.xstylesheet.psi.PsiXStyleSheetFunctionCallParameterList;

import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 2013-10-08
 */
public class CssFunctionCallParameterList extends CssElement implements PsiXStyleSheetFunctionCallParameterList {
    public CssFunctionCallParameterList(@Nonnull ASTNode node) {
        super(node);
    }

    @Nonnull
    @RequiredReadAction
    @Override
    public PsiElement[] getParameters() {
        return findChildrenByType(TokenSet.create(CssTokens.STRING, CssTokens.NUMBER, CssTokens.IDENTIFIER), PsiElement.class);
    }
}
