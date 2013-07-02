package org.consulo.css.lang;

import javax.swing.Icon;

import org.consulo.css.CssIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.fileTypes.LanguageFileType;

/**
 * @author VISTALL
 * @since 23:57/12.06.13
 */
public class CssFileType extends LanguageFileType {
    public static final CssFileType INSTANCE = new CssFileType();

    private CssFileType() {
        super(CssLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "CSS";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "CSS";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "css";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return CssIcons.CssFile;
    }
}
