package consulo.css.lang.psi;

import consulo.annotation.access.RequiredReadAction;
import consulo.annotation.access.RequiredWriteAction;
import consulo.css.lang.CssTokens;
import consulo.document.util.TextRange;
import consulo.language.ast.ASTNode;
import consulo.language.psi.*;
import consulo.language.psi.resolve.ResolveCache;
import consulo.language.util.IncorrectOperationException;
import consulo.xstylesheet.psi.PsiXStyleSheetVariableReference;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author VISTALL
 * @since 2024-03-14
 */
public class CssVariableReference extends CssElement implements PsiXStyleSheetVariableReference {
    public CssVariableReference(@Nonnull ASTNode node) {
        super(node);
    }

    @RequiredReadAction
    @Override
    @Nonnull
    public PsiElement getElement() {
        return findNotNullChildByType(CssTokens.IDENTIFIER);
    }

    @Override
    public PsiReference getReference() {
        return this;
    }

    @RequiredReadAction
    @Nonnull
    @Override
    public TextRange getRangeInElement() {
        PsiElement element = getElement();
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
    @Nonnull
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
    public PsiElement bindToElement(@Nonnull PsiElement psiElement) throws IncorrectOperationException {
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

                    if (element instanceof CssVariable cssVariable) {
                        consumer.accept(cssVariable);
                    }
                }
            });
        }
    }

    @RequiredReadAction
    @Nonnull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode) {
        return ResolveCache.getInstance(getProject()).resolveWithCaching(
            this,
            (ref, in) -> {
                List<ResolveResult> results = new ArrayList<>();
                String refText = ref.getCanonicalText();
                ref.visitVariants(element ->
                {
                    if (element instanceof CssVariable cssVariable && Objects.equals(refText, cssVariable.getName())) {
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
