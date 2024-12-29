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
import consulo.language.ast.ASTNode;
import consulo.xstylesheet.psi.XStyleSheetSelector;
import consulo.xstylesheet.psi.XStyleSheetSelectorList;

import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 2017-08-20
 */
public class CssSelectorListImpl extends CssElement implements XStyleSheetSelectorList {
    public CssSelectorListImpl(@Nonnull ASTNode node) {
        super(node);
    }

    @RequiredReadAction
    @Nonnull
    @Override
    public XStyleSheetSelector[] getSelectors() {
        return findChildrenByClass(XStyleSheetSelector.class);
    }
}
