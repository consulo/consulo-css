/*
 * Copyright 2013-2026 consulo.io
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

import consulo.annotation.component.ComponentScope;
import consulo.annotation.component.ExtensionAPI;
import consulo.component.extension.ExtensionPointName;
import consulo.fileEditor.structureView.StructureViewTreeElement;
import consulo.language.psi.PsiElement;

import java.util.Collection;

/**
 * Lets stylesheet dialects show their own elements (at-rules, ...) in the structure view of a stylesheet.
 *
 * @since 2026-10-26
 */
@ExtensionAPI(ComponentScope.APPLICATION)
public interface CssStructureViewElementsProvider {
    ExtensionPointName<CssStructureViewElementsProvider> EP_NAME = ExtensionPointName.create(CssStructureViewElementsProvider.class);

    boolean isMyContext(PsiElement element);

    /**
     * @return structure view elements for a top-level element of the stylesheet, empty to use the default presentation
     */
    Collection<? extends StructureViewTreeElement> createStructureViewElements(PsiElement element);
}
