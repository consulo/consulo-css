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
import consulo.annotation.access.RequiredWriteAction;
import consulo.css.lang.CssTokens;
import consulo.language.ast.ASTNode;
import consulo.language.impl.psi.ASTWrapperPsiElement;
import consulo.language.psi.PsiElement;
import consulo.language.util.IncorrectOperationException;
import consulo.xstylesheet.definition.XStyleSheetProperty;
import consulo.xstylesheet.psi.PsiXStyleSheetPropertyValuePart;
import consulo.xstylesheet.psi.PsiXStyleSheetVariable;

import org.jspecify.annotations.Nullable;

/**
 * PSI element for SCSS variable declarations: {@code $name: value;}
 * Implements {@link PsiXStyleSheetVariable} so that property validation inspections
 * correctly skip these elements (they are variables, not CSS properties).
 *
 * @author VISTALL
 * @since 2026-03-16
 */
public class ScssVariableDeclaration extends ASTWrapperPsiElement implements PsiXStyleSheetVariable {
    public ScssVariableDeclaration(ASTNode node) {
        super(node);
    }

    @Nullable
    @RequiredReadAction
    @Override
    public XStyleSheetProperty getXStyleSheetProperty() {
        // SCSS variables are not CSS properties
        return null;
    }

    @Override
    public PsiXStyleSheetPropertyValuePart[] getParts() {
        return findChildrenByClass(PsiXStyleSheetPropertyValuePart.class);
    }

    @Nullable
    @RequiredReadAction
    @Override
    public PsiElement getNameIdentifier() {
        return findChildByType(CssTokens.IDENTIFIER);
    }

    @Nullable
    @RequiredReadAction
    @Override
    public String getName() {
        PsiElement nameIdentifier = getNameIdentifier();
        return nameIdentifier == null ? null : nameIdentifier.getText();
    }

    @RequiredReadAction
    @Override
    public boolean isImportant() {
        return findChildByType(CssTokens.IMPORTANT_KEYWORD) != null;
    }

    @RequiredWriteAction
    @Override
    public PsiElement setName(String s) throws IncorrectOperationException {
        return null;
    }
}
