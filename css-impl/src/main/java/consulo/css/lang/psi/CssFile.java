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

package consulo.css.lang.psi;

import consulo.annotation.access.RequiredReadAction;
import consulo.css.lang.CssLanguage;
import consulo.language.file.FileViewProvider;
import consulo.language.impl.psi.PsiFileBase;
import consulo.language.psi.PsiElementVisitor;
import consulo.xstylesheet.psi.PsiXStyleSheetRule;
import consulo.xstylesheet.psi.XStyleSheetFile;
import consulo.xstylesheet.psi.XStyleSheetRoot;
import consulo.xstylesheet.psi.reference.nameResolving.XStyleRuleCondition;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.List;
import java.util.Objects;

/**
 * @author VISTALL
 * @since 2013-07-02
 */
public class CssFile extends PsiFileBase implements XStyleSheetFile {
    public CssFile(@Nonnull FileViewProvider provider) {
        super(provider, CssLanguage.INSTANCE);
    }

    @Override
    public void accept(@Nonnull PsiElementVisitor psiElementVisitor) {
        psiElementVisitor.visitFile(this);
    }

    @Nullable
    @RequiredReadAction
    public PsiXStyleSheetRule findRule(@Nonnull XStyleRuleCondition condition) {
        return getRoot().findRule(condition);
    }

    @Nonnull
    @RequiredReadAction
    public List<PsiXStyleSheetRule> findRules(@Nonnull XStyleRuleCondition condition) {
        return getRoot().findRules(condition);
    }

    @Nonnull
    @Override
    public XStyleSheetRoot getRoot() {
        return Objects.requireNonNull(findChildByClass(XStyleSheetRoot.class));
    }
}
