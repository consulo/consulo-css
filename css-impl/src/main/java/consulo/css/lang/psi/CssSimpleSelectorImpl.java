/*
 * Copyright 2013-2017 must-be.org
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
import consulo.language.ast.IElementType;
import consulo.language.ast.TokenSet;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiUtilCore;
import consulo.xstylesheet.psi.XStyleSheetSimpleSelector;
import consulo.xstylesheet.psi.XStyleSheetSimpleSelectorType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author VISTALL
 * @since 2017-08-20
 */
public class CssSimpleSelectorImpl extends CssElement implements XStyleSheetSimpleSelector {
    private static final TokenSet nameSet =
        TokenSet.create(CssTokens.IDENTIFIER, CssTokens.SELECTOR_CLASS, CssTokens.SELECTOR_ID, CssTokens.ASTERISK);

    public CssSimpleSelectorImpl(@Nonnull ASTNode node) {
        super(node);
    }

    @RequiredReadAction
    @Nullable
    @Override
    public PsiElement getElement() {
        return findChildByType(nameSet);
    }

    @RequiredReadAction
    @Override
    public String getName() {
        PsiElement element = findChildByType(nameSet);
        if (element != null) {
            String text = element.getText();
            IElementType type = PsiUtilCore.getElementType(element);
            if (type == CssTokens.IDENTIFIER || type == CssTokens.ASTERISK) {
                return text;
            }
            return text.substring(1, text.length());
        }
        return null;
    }

    @RequiredReadAction
    @Nonnull
    @Override
    public XStyleSheetSimpleSelectorType getType() {
        PsiElement element = findChildByType(nameSet);
        IElementType type = PsiUtilCore.getElementType(element);
        if (type == CssTokens.IDENTIFIER) {
            return XStyleSheetSimpleSelectorType.TAG;
        }
        else if (type == CssTokens.SELECTOR_CLASS) {
            return XStyleSheetSimpleSelectorType.CLASS;
        }
        else if (type == CssTokens.SELECTOR_ID) {
            return XStyleSheetSimpleSelectorType.ID;
        }
        else if (type == CssTokens.ASTERISK) {
            return XStyleSheetSimpleSelectorType.ANY;
        }
        return XStyleSheetSimpleSelectorType.ANY;
    }
}
