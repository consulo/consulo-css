package consulo.css.lang.psi;

import consulo.annotation.access.RequiredReadAction;
import consulo.annotation.access.RequiredWriteAction;
import consulo.css.lang.CssTokens;
import consulo.css.lang.index.CssCustomPropertyIndex;
import consulo.document.util.TextRange;
import consulo.language.ast.ASTNode;
import consulo.language.psi.*;
import consulo.language.psi.resolve.ResolveCache;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.language.psi.stub.FileBasedIndex;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.language.util.IncorrectOperationException;
import consulo.project.Project;
import consulo.virtualFileSystem.VirtualFile;
import consulo.xstylesheet.psi.PsiXStyleSheetVariableReference;
import consulo.xstylesheet.psi.index.XStyleSheetDeclarations;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author VISTALL
 * @since 2024-03-14
 */
public class CssVariableReference extends CssElement implements PsiXStyleSheetVariableReference {
    public CssVariableReference(ASTNode node) {
        super(node);
    }

    @RequiredReadAction
    @Override
    public PsiElement getElement() {
        return findNotNullChildByType(CssTokens.IDENTIFIER);
    }

    @Override
    public PsiReference getReference() {
        return this;
    }

    @RequiredReadAction
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

                    if (element instanceof CssVariable cssVariable) {
                        consumer.accept(cssVariable);
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
                ref.visitVariants(element ->
                {
                    if (element instanceof CssVariable cssVariable && Objects.equals(refText, cssVariable.getName())) {
                        results.add(new PsiElementResolveResult(element, true));
                    }
                });
                if (results.isEmpty()) {
                    // declared in another stylesheet: the theme of a CSS framework, or :root of a shared file. Stylesheets
                    // are seldom in source roots, so the whole project is searched
                    for (CssVariable variable : XStyleSheetDeclarations.preferProjectOwn(findDeclarations(ref.getProject(), refText, GlobalSearchScope.projectScope(ref.getProject())))) {
                        results.add(new PsiElementResolveResult(variable, true));
                    }
                }
                return results.toArray(ResolveResult.EMPTY_ARRAY);
            },
            true,
            incompleteCode
        );
    }

    /**
     * The declarations of the custom property {@code name} in the stylesheets of {@code scope}, found with
     * {@link CssCustomPropertyIndex}
     */
    @RequiredReadAction
    public static List<CssVariable> findDeclarations(Project project, String name, GlobalSearchScope scope) {
        List<CssVariable> result = new ArrayList<>();
        PsiManager psiManager = PsiManager.getInstance(project);
        for (VirtualFile file : FileBasedIndex.getInstance().getContainingFiles(CssCustomPropertyIndex.INDEX_ID, name, scope)) {
            PsiFile psiFile = psiManager.findFile(file);
            if (psiFile == null) {
                continue;
            }
            for (CssVariable variable : PsiTreeUtil.findChildrenOfType(psiFile, CssVariable.class)) {
                if (name.equals(variable.getName())) {
                    result.add(variable);
                }
            }
        }
        return result;
    }
}
