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

import consulo.css.lang.CssLanguage;
import consulo.language.Language;
import consulo.language.ast.ASTNode;
import consulo.language.impl.ast.LeafElement;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import org.jspecify.annotations.Nullable;

/**
 * PSI helpers for CSS and its dialects.
 *
 * @since 2026-10-25
 */
public final class CssPsiUtilCore {
    private CssPsiUtilCore() {
    }

    /**
     * Replaces the text of a token, keeping its element type.
     */
    public static PsiElement replaceToken(PsiElement token, String newText) {
        ASTNode node = token.getNode();
        if (node instanceof LeafElement leaf) {
            return leaf.replaceWithText(newText).getPsi();
        }
        return token;
    }

    /**
     * Language of the stylesheet containing the element: CSS or one of its dialects.
     */
    public static @Nullable Language getStylesheetLanguage(@Nullable PsiElement element) {
        if (element == null) {
            return null;
        }
        PsiFile file = element.getContainingFile();
        if (file == null) {
            return null;
        }
        Language language = file.getLanguage();
        return language.isKindOf(CssLanguage.INSTANCE) ? language : null;
    }

    public static String getTokenText(@Nullable PsiElement element) {
        return element == null ? "" : element.getText();
    }
}
