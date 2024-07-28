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

import consulo.language.impl.psi.FakePsiElement;
import consulo.language.psi.PsiElement;
import consulo.xstylesheet.psi.reference.BuildInSymbolElement;

/**
 * @author VISTALL
 * @since 20-Aug-17
 */
public class BuildInSymbolElementImpl extends FakePsiElement implements BuildInSymbolElement {
    private final PsiElement myParent;

    public BuildInSymbolElementImpl(PsiElement parent) {
        myParent = parent;
    }

    @Override
    public boolean canNavigate() {
        return false;
    }

    @Override
    public boolean canNavigateToSource() {
        return false;
    }

    @Override
    public PsiElement getParent() {
        return myParent;
    }
}
