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

import consulo.annotation.component.ExtensionImpl;
import consulo.codeEditor.Editor;
import consulo.css.lang.CssLanguage;
import consulo.css.lang.psi.CssFile;
import consulo.fileEditor.structureView.StructureViewBuilder;
import consulo.fileEditor.structureView.StructureViewModel;
import consulo.fileEditor.structureView.TreeBasedStructureViewBuilder;
import consulo.language.Language;
import consulo.language.editor.structureView.PsiStructureViewFactory;
import consulo.language.editor.structureView.StructureViewModelBase;
import consulo.language.psi.PsiFile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author VISTALL
 * @since 2013-07-08
 */
@ExtensionImpl
public class CssPsiStructureViewFactory implements PsiStructureViewFactory {
    @Nullable
    @Override
    public StructureViewBuilder getStructureViewBuilder(final PsiFile psiFile) {
        return new TreeBasedStructureViewBuilder() {
            @Nonnull
            @Override
            public StructureViewModel createStructureViewModel(Editor editor) {
                return new StructureViewModelBase(psiFile, new CssStructureViewTreeElement((CssFile)psiFile));
            }
        };
    }

    @Nonnull
    @Override
    public Language getLanguage() {
        return CssLanguage.INSTANCE;
    }
}
