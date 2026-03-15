package consulo.css.lang.psi;

import consulo.language.ast.ASTNode;
import consulo.xstylesheet.psi.PsiXStyleSheetVariable;


/**
 * @author VISTALL
 * @since 2024-03-14
 */
public class CssVariable extends CssProperty implements PsiXStyleSheetVariable {
    public CssVariable(ASTNode node) {
        super(node);
    }
}
