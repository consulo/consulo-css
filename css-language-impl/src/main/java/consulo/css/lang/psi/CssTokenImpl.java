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

package consulo.css.lang.psi;

import consulo.language.ast.IElementType;
import consulo.language.impl.psi.LeafPsiElement;
import consulo.language.psi.ContributedReferenceHost;
import consulo.language.psi.PsiReference;
import consulo.language.psi.PsiReferenceService;

/**
 * CSS token leaf which accepts references from {@code PsiReferenceContributor}s, so dialects (PostCSS, ...) and other plugins
 * can attach references to identifiers without own PSI elements.
 *
 * @since 2026-10-25
 */
public class CssTokenImpl extends LeafPsiElement implements ContributedReferenceHost {
    public CssTokenImpl(IElementType type, CharSequence text) {
        super(type, text);
    }

    @Override
    public PsiReference[] getReferences() {
        return PsiReferenceService.getService().getContributedReferences(this);
    }
}
