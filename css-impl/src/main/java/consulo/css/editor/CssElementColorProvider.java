package consulo.css.editor;

import consulo.annotation.component.ExtensionImpl;
import consulo.css.lang.CssLanguage;
import consulo.language.Language;
import consulo.xstylesheet.highlight.XStyleSheetElementColorProvider;
import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 2025-05-31
 */
@ExtensionImpl
public class CssElementColorProvider extends XStyleSheetElementColorProvider {
    @Nonnull
    @Override
    public Language getLanguage() {
        return CssLanguage.INSTANCE;
    }
}
