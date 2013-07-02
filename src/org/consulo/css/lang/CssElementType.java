package org.consulo.css.lang;

import com.intellij.lang.Language;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author VISTALL
 * @since 02.07.13.
 */
public class CssElementType extends IElementType {
    public CssElementType(@NotNull @NonNls String debugName) {
        super(debugName, CssLanguage.INSTANCE);
    }
}
