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

import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiElement;
import org.jspecify.annotations.Nullable;

/**
 * At-rule without dedicated support: {@code @keyword prelude { block }} or {@code @keyword prelude;}.
 *
 * @since 2026-10-25
 */
public class CssAtRuleImpl extends CssElement implements CssAtRule {
    public CssAtRuleImpl(ASTNode node) {
        super(node);
    }

    /**
     * @return at-rule name without "@", e.g. "supports"
     */
    public String getAtRuleName() {
        PsiElement keyword = getFirstChild();
        return keyword == null ? "" : keyword.getText().substring(1);
    }

    public @Nullable CssBlock getBlock() {
        return findChildByClass(CssBlock.class);
    }
}
