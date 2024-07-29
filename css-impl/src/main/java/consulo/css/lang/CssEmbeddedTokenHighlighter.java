package consulo.css.lang;

import com.intellij.xml.highlighter.EmbeddedTokenHighlighter;
import consulo.annotation.component.ExtensionImpl;
import consulo.colorScheme.TextAttributesKey;
import consulo.language.ast.IElementType;
import consulo.util.collection.MultiMap;
import consulo.xml.lang.xml.XMLLanguage;
import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 2024-04-18
 */
@ExtensionImpl
public class CssEmbeddedTokenHighlighter implements EmbeddedTokenHighlighter {
    @Nonnull
    @Override
    public MultiMap<IElementType, TextAttributesKey> getEmbeddedTokenAttributes(@Nonnull XMLLanguage xmlLanguage) {
        MultiMap<IElementType, TextAttributesKey> result = MultiMap.createLinked();
        CssSyntaxHighlighter.storeDefaults(result);
        return result;
    }
}
