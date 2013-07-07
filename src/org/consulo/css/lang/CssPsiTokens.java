package org.consulo.css.lang;

import com.intellij.psi.tree.IElementType;
import org.consulo.css.lang.psi.*;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public interface CssPsiTokens {
    IElementType RULE = new CssPsiElementType("RULE", CssRule.class);

    IElementType BLOCK = new CssPsiElementType("BLOCK", CssBlock.class);

    IElementType PROPERTY = new CssPsiElementType("PROPERTY", CssProperty.class);

    IElementType PROPERTY_VALUE_PART = new CssPsiElementType("PROPERTY_VALUE_PART", CssPropertyValuePart.class);

    IElementType SELECTOR_REFERENCE = new CssPsiElementType("SELECTOR_REFERENCE", CssSelectorReference.class);

    IElementType SELECTOR_ELEMENT = new CssPsiElementType("SELECTOR_ELEMENT", CssSelectorElement.class);

    IElementType SELECTOR_CLASS = new CssPsiElementType("SELECTOR_CLASS", CssSelectorClass.class);

    IElementType SELECTOR_ID = new CssPsiElementType("SELECTOR_ID", CssSelectorId.class);
}
