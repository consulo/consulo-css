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
package consulo.css.editor.structureView;

import consulo.annotation.access.RequiredReadAction;
import consulo.fileEditor.structureView.StructureViewTreeElement;
import consulo.language.editor.structureView.PsiTreeElementBase;
import consulo.localize.LocalizeValue;
import consulo.xstylesheet.psi.XStyleSheetSelector;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * @author VISTALL
 * @since 2013-07-08
 */
public class CssSelectorDeclarationStructureViewTreeElement extends PsiTreeElementBase<XStyleSheetSelector> {
    public CssSelectorDeclarationStructureViewTreeElement(XStyleSheetSelector psiElement) {
        super(psiElement);
    }

    @Override
    public Collection<StructureViewTreeElement> getChildrenBase() {
        return Collections.emptyList();
    }

    @Override
    @RequiredReadAction
    public LocalizeValue getPresentableText() {
        return LocalizeValue.of(Objects.requireNonNull(getElement()).getText());
    }
}
