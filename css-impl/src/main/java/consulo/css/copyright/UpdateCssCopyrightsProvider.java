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

package consulo.css.copyright;

import consulo.annotation.component.ExtensionImpl;
import consulo.css.lang.CssFileType;
import consulo.language.copyright.BaseUpdateCopyrightsProvider;
import consulo.language.copyright.UpdatePsiFileCopyright;
import consulo.language.copyright.config.CopyrightFileConfig;
import consulo.language.copyright.config.CopyrightProfile;
import consulo.language.psi.PsiComment;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiWhiteSpace;
import consulo.virtualFileSystem.fileType.FileType;

/**
 * Copyright notice of stylesheets: a block comment before the first rule. Dialects extend it with their file type.
 *
 * @since 2026-10-26
 */
@ExtensionImpl
public class UpdateCssCopyrightsProvider extends BaseUpdateCopyrightsProvider {
    @Override
    public FileType getFileType() {
        return CssFileType.INSTANCE;
    }

    @Override
    public UpdatePsiFileCopyright<CopyrightFileConfig> createInstance(PsiFile file, CopyrightProfile copyrightProfile) {
        return new UpdatePsiFileCopyright<>(file, copyrightProfile) {
            @Override
            protected void scanFile() {
                PsiElement first = getFile().getFirstChild();
                PsiElement root = first;
                // the stylesheet root holds the comments and rules
                if (root != null && !(root instanceof PsiComment) && !(root instanceof PsiWhiteSpace) && root.getFirstChild() != null) {
                    first = root.getFirstChild();
                }
                PsiElement last = first;
                while (last != null && (last instanceof PsiComment || last instanceof PsiWhiteSpace)) {
                    last = last.getNextSibling();
                }
                checkComments(first, last, true);
            }
        };
    }

    @Override
    public CopyrightFileConfig createDefaultOptions() {
        CopyrightFileConfig options = super.createDefaultOptions();
        // CSS has block comments only
        options.setBlock(true);
        return options;
    }
}
