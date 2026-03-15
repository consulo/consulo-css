package consulo.css.html;

import com.intellij.xml.highlighter.EmbeddedTokenHighlighter;
import consulo.annotation.component.ExtensionImpl;
import consulo.colorScheme.TextAttributesKey;
import consulo.css.lang.CssSyntaxHighlighter;
import consulo.language.ast.IElementType;
import consulo.util.collection.MultiMap;
import consulo.xml.lang.xml.XMLLanguage;

/**
 * @author VISTALL
 * @since 2024-04-18
 */
@ExtensionImpl
public class CssEmbeddedTokenHighlighter implements EmbeddedTokenHighlighter {
    @Override
    public MultiMap<IElementType, TextAttributesKey> getEmbeddedTokenAttributes(XMLLanguage xmlLanguage) {
        MultiMap<IElementType, TextAttributesKey> result = MultiMap.createLinked();
        CssSyntaxHighlighter.storeDefaults(result);
        return result;
    }
}
