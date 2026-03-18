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
import consulo.document.util.TextRange;
import consulo.language.ast.ASTNode;
import consulo.language.impl.psi.ASTWrapperPsiElement;
import consulo.language.psi.*;
import consulo.language.psi.resolve.ResolveCache;
import consulo.language.util.IncorrectOperationException;
import consulo.xstylesheet.psi.PsiXStyleSheetVariableReference;

import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * PSI element for SCSS variable references: {@code $name} in values.
 * Implements {@link PsiXStyleSheetVariableReference} to enable reference resolution
 * and to mark property value parts as "soft" (skipping validation).
 *
 * @author VISTALL
 * @since 2026-03-16
 */
public class ScssVariableReference extends ASTWrapperPsiElement implements PsiXStyleSheetVariableReference {
    public ScssVariableReference(ASTNode node) {
        super(node);
    }

    @Override
    public PsiReference getReference() {
        return this;
    }

    @RequiredReadAction
    @Override
    public PsiElement getElement() {
        PsiElement identifier = findChildByType(CssTokens.IDENTIFIER);
        return identifier != null ? identifier : this;
    }

    @RequiredReadAction
    @Override
    public TextRange getRangeInElement() {
        PsiElement element = getElement();
        if (element == this) {
            return new TextRange(0, getTextLength());
        }
        return new TextRange(0, element.getTextLength());
    }

    @RequiredReadAction
    @Nullable
    @Override
    public PsiElement resolve() {
        ResolveResult[] resolveResults = multiResolve(false);
        for (ResolveResult resolveResult : resolveResults) {
            if (resolveResult.isValidResult()) {
                return resolveResult.getElement();
            }
        }
        return null;
    }

    @RequiredReadAction
    @Override
    public String getCanonicalText() {
        PsiElement element = getElement();
        return element.getText();
    }

    @RequiredWriteAction
    @Override
    public PsiElement handleElementRename(String s) throws IncorrectOperationException {
        return null;
    }

    @RequiredWriteAction
    @Override
    public PsiElement bindToElement(PsiElement psiElement) throws IncorrectOperationException {
        return null;
    }

    @RequiredReadAction
    @Override
    public boolean isReferenceTo(PsiElement target) {
        PsiManager manager = getManager();
        for (ResolveResult result : multiResolve(false)) {
            PsiElement element = result.getElement();
            if (manager.areElementsEquivalent(element, target)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void visitVariants(Consumer<PsiNameIdentifierOwner> consumer) {
        PsiFile containingFile = getContainingFile();
        if (containingFile != null) {
            containingFile.accept(new PsiRecursiveElementVisitor() {
                @Override
                public void visitElement(PsiElement element) {
                    super.visitElement(element);

                    if (element instanceof ScssVariableDeclaration scssVariable) {
                        consumer.accept(scssVariable);
                    }
                }
            });
        }
    }

    @RequiredReadAction
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode) {
        return ResolveCache.getInstance(getProject()).resolveWithCaching(
            this,
            (ref, in) -> {
                List<ResolveResult> results = new ArrayList<>();
                String refText = ref.getCanonicalText();
                ref.visitVariants(element -> {
                    if (element instanceof ScssVariableDeclaration scssVariable
                        && Objects.equals(refText, scssVariable.getName())) {
                        results.add(new PsiElementResolveResult(element, true));
                    }
                });
                return results.toArray(ResolveResult.EMPTY_ARRAY);
            },
            true,
            incompleteCode
        );
    }
}
