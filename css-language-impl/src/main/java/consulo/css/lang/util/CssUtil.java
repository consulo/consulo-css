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

package consulo.css.lang.util;

import consulo.css.lang.CssTokens;
import consulo.css.lang.psi.CssImport;
import consulo.language.ast.ASTNode;
import consulo.language.ast.IElementType;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiManager;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.util.lang.StringUtil;
import consulo.virtualFileSystem.VirtualFile;
import org.jspecify.annotations.Nullable;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @since 2026-10-25
 */
public final class CssUtil {
    private CssUtil() {
    }

    /**
     * Scope for completing and resolving names declared in stylesheets (custom selectors, custom media, ...).
     */
    public static GlobalSearchScope getCompletionAndResolvingScopeForElement(PsiElement context) {
        return context.getResolveScope();
    }

    /**
     * Files imported by {@code @import "path"} / {@code @import url(path)} of the file, resolved relative to it.
     *
     * @param context   element of the file the imports are requested for
     * @param recursive also collect imports of imported files
     */
    public static Set<VirtualFile> getImportedFiles(PsiFile file, PsiElement context, boolean recursive) {
        Set<VirtualFile> result = new LinkedHashSet<>();
        collectImportedFiles(file, recursive, result);
        return result;
    }

    private static void collectImportedFiles(PsiFile file, boolean recursive, Set<VirtualFile> result) {
        VirtualFile virtualFile = file.getOriginalFile().getVirtualFile();
        VirtualFile directory = virtualFile == null ? null : virtualFile.getParent();
        if (directory == null) {
            return;
        }

        for (CssImport cssImport : PsiTreeUtil.findChildrenOfType(file, CssImport.class)) {
            String path = getImportPath(cssImport);
            if (path == null) {
                continue;
            }
            VirtualFile imported = directory.findFileByRelativePath(path);
            if (imported == null || !result.add(imported) || !recursive) {
                continue;
            }
            PsiFile importedPsi = PsiManager.getInstance(file.getProject()).findFile(imported);
            if (importedPsi != null) {
                collectImportedFiles(importedPsi, true, result);
            }
        }
    }

    private static @Nullable String getImportPath(CssImport cssImport) {
        // @import "path"; or @import url(path); / url("path");
        ASTNode path = findFirst(cssImport.getNode(), CssTokens.STRING, CssTokens.URL_TOKEN);
        return path == null ? null : unquote(path.getText());
    }

    private static @Nullable ASTNode findFirst(ASTNode node, IElementType... types) {
        for (ASTNode child = node.getFirstChildNode(); child != null; child = child.getTreeNext()) {
            for (IElementType type : types) {
                if (child.getElementType() == type) {
                    return child;
                }
            }
            ASTNode found = findFirst(child, types);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    private static String unquote(String text) {
        return StringUtil.unquoteString(text.trim());
    }
}
