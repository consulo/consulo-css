package consulo.css.lang.psi;

import consulo.annotation.access.RequiredReadAction;
import consulo.language.ast.ASTNode;
import consulo.language.impl.psi.ASTWrapperPsiElement;
import consulo.xstylesheet.psi.PsiXStyleSheetRule;
import consulo.xstylesheet.psi.XStyleSheetRoot;

import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 2017-08-20
 */
public class CssRoot extends ASTWrapperPsiElement implements XStyleSheetRoot {
    public CssRoot(@Nonnull ASTNode node) {
        super(node);
    }

    @RequiredReadAction
    @Nonnull
    @Override
    public PsiXStyleSheetRule[] getRules() {
        return findChildrenByClass(PsiXStyleSheetRule.class);
    }
}
