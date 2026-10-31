package consulo.scss.html;

import consulo.annotation.component.ExtensionImpl;
import consulo.css.html.psi.CssStylesheetLazyElementType;
import consulo.css.lang.parser.CssParser;
import consulo.language.ast.IElementType;
import consulo.language.lexer.Lexer;
import consulo.scss.lang.ScssLanguage;
import consulo.scss.lang.lexer._ScssLexer;
import consulo.scss.lang.parser.ScssParser;
import consulo.xml.lexer.EmbeddedTokenTypesProvider;

/**
 * SCSS embedded into HTML: {@code <style type="text/x-scss">}, {@code <style lang="scss">} of Vue single-file components.
 *
 * @since 2026-10-31
 */
@ExtensionImpl
public class ScssEmbeddedTokenTypesProvider implements EmbeddedTokenTypesProvider {
    public static final IElementType SCSS_LAZY_STYLESHEET = new CssStylesheetLazyElementType("SCSS_LAZY_STYLESHEET", ScssLanguage.INSTANCE) {
        @Override
        protected Lexer createLexer() {
            return new _ScssLexer();
        }

        @Override
        protected CssParser createParser() {
            return new ScssParser();
        }
    };

    @Override
    public String getName() {
        return "scss";
    }

    @Override
    public IElementType getElementType() {
        return SCSS_LAZY_STYLESHEET;
    }
}
