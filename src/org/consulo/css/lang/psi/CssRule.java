package org.consulo.css.lang.psi;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class CssRule extends CssElement {
    public CssRule(@NotNull ASTNode node) {
        super(node);
    }
}
