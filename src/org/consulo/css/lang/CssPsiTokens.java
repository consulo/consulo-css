package org.consulo.css.lang;

import com.intellij.psi.tree.IElementType;
import org.consulo.css.lang.psi.CssProperty;
import org.consulo.css.lang.psi.CssRule;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public interface CssPsiTokens {
    IElementType RULE = new CssPsiElementType("RULE", CssRule.class);

    IElementType PROPERTY = new CssPsiElementType("PROPERTY", CssProperty.class);
}
