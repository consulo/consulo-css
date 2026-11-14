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
package consulo.xstylesheet.psi.index;

import consulo.annotation.access.RequiredReadAction;
import consulo.language.psi.PsiElement;
import consulo.virtualFileSystem.VirtualFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Picks the declarations a stylesheet reference resolves to among those of the whole project: the project's own, when
 * it has any, before those of the packages it installs ({@code vendor}, {@code node_modules}), which are often in the
 * project content and declare the same class names and custom properties for their own pages
 *
 * @since 2026-11-06
 */
public final class XStyleSheetDeclarations {
    private static final List<String> PACKAGE_DIRECTORIES = List.of("vendor", "node_modules");

    private XStyleSheetDeclarations() {
    }

    @RequiredReadAction
    public static <T extends PsiElement> List<T> preferProjectOwn(List<T> declarations) {
        List<T> own = new ArrayList<>(declarations.size());
        for (T declaration : declarations) {
            if (!isInPackage(declaration)) {
                own.add(declaration);
            }
        }
        return own.isEmpty() ? declarations : own;
    }

    @RequiredReadAction
    private static boolean isInPackage(PsiElement element) {
        VirtualFile file = element.getContainingFile() == null ? null : element.getContainingFile().getVirtualFile();
        for (VirtualFile directory = file == null ? null : file.getParent(); directory != null; directory = directory.getParent()) {
            if (PACKAGE_DIRECTORIES.contains(directory.getName())) {
                return true;
            }
        }
        return false;
    }
}
