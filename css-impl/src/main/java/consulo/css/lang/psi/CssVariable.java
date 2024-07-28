package consulo.css.lang.psi;

import consulo.language.ast.ASTNode;
import consulo.xstylesheet.psi.PsiXStyleSheetVariable;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 2024-03-14
 */
public class CssVariable extends CssProperty implements PsiXStyleSheetVariable {
    public CssVariable(@Nonnull ASTNode node) {
        super(node);
    }
}
