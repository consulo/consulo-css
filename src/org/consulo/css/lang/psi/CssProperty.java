package org.consulo.css.lang.psi;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class CssProperty extends CssElement {
    public CssProperty(@NotNull ASTNode node) {
        super(node);
    }
}
