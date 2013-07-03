package org.consulo.css.lang;

import com.intellij.lang.ASTNode;
import com.intellij.util.ReflectionUtil;
import org.consulo.css.lang.psi.CssElement;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class CssPsiElementType extends CssElementType {
    private final Constructor<? extends CssElement> myConstructor;

    public CssPsiElementType(@NotNull @NonNls String debugName, @NotNull Class<? extends CssElement> clazz) {
        super(debugName);
        try {
            myConstructor = clazz.getConstructor(ASTNode.class);
        } catch (NoSuchMethodException e) {
            throw new Error(e);
        }
    }

    @NotNull
    public CssElement createPsi(@NotNull ASTNode astNode) {
        return ReflectionUtil.createInstance(myConstructor, astNode);
    }
}
