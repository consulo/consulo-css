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
import consulo.xstylesheet.psi.PsiXStyleSheetRule;
import consulo.xstylesheet.psi.XStyleSheetSelector;
import consulo.xstylesheet.psi.XStyleSheetSelectorList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class CssRule extends CssElement implements PsiXStyleSheetRule {
    public CssRule(@Nonnull ASTNode node) {
        super(node);
    }

    @Nullable
    @Override
    public CssBlock getBlock() {
        return findChildByClass(CssBlock.class);
    }

    @Nonnull
    @Override
    public CssProperty[] getProperties() {
        CssBlock block = getBlock();
        return block == null ? CssProperty.EMPTY_ARRAY : block.getProperties();
    }

    @RequiredReadAction
    @Nullable
    @Override
    public XStyleSheetSelectorList getSelectorList() {
        return findChildByClass(XStyleSheetSelectorList.class);
    }

    @RequiredReadAction
    @Override
    public XStyleSheetSelector[] getSelectors() {
        XStyleSheetSelectorList selectorList = getSelectorList();
        return selectorList == null ? XStyleSheetSelector.EMPTY_ARRAY : selectorList.getSelectors();
    }
}
