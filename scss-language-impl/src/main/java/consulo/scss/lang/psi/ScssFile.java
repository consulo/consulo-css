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

package consulo.scss.lang.psi;

import consulo.annotation.access.RequiredReadAction;
import consulo.language.file.FileViewProvider;
import consulo.language.impl.psi.PsiFileBase;
import consulo.language.psi.PsiElementVisitor;
import consulo.scss.lang.ScssLanguage;
import consulo.xstylesheet.psi.PsiXStyleSheetRule;
import consulo.xstylesheet.psi.XStyleSheetFile;
import consulo.xstylesheet.psi.XStyleSheetRoot;
import consulo.xstylesheet.psi.reference.nameResolving.XStyleRuleCondition;

import org.jspecify.annotations.Nullable;
import java.util.List;
import java.util.Objects;

/**
 * @author VISTALL
 * @since 2026-03-16
 */
public class ScssFile extends PsiFileBase implements XStyleSheetFile {
    public ScssFile(FileViewProvider provider) {
        super(provider, ScssLanguage.INSTANCE);
    }

    @Override
    public void accept(PsiElementVisitor psiElementVisitor) {
        psiElementVisitor.visitFile(this);
    }

    @Nullable
    @RequiredReadAction
    public PsiXStyleSheetRule findRule(XStyleRuleCondition condition) {
        return getRoot().findRule(condition);
    }

    @RequiredReadAction
    public List<PsiXStyleSheetRule> findRules(XStyleRuleCondition condition) {
        return getRoot().findRules(condition);
    }

    @Override
    public XStyleSheetRoot getRoot() {
        return Objects.requireNonNull(findChildByClass(XStyleSheetRoot.class));
    }
}
