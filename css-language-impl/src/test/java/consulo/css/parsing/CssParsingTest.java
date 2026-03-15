package consulo.css.parsing;

import consulo.css.lang.CssFileType;
import consulo.language.file.LanguageFileType;
import consulo.test.junit.impl.language.SimpleParsingTest;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.junit.jupiter.api.Test;

/**
 * @author VISTALL
 * @since 2013-07-02
 */
public class CssParsingTest extends SimpleParsingTest<Object> {
    public CssParsingTest() {
        super("parsing", "css");
    }

    @Test
    public void testCssParsing1(Context context) throws Exception {
        doTest(context, null);
    }

    @Test
    public void testCssParsingSelectorList(Context context) throws Exception {
        doTest(context, null);
    }

    @Test
    public void testAsterisk(Context context) throws Exception {
        doTest(context, null);
    }

    @Test
    public void testMultiSelectors(Context context) throws Exception {
        doTest(context, null);
    }

    @Test
    public void testSelectorAttribute(Context context) throws Exception {
        doTest(context, null);
    }

    @Test
    public void testWiki(Context context) throws Exception {
        doTest(context, null);
    }

    @Test
    public void testUrl(Context context) throws Exception {
        doTest(context, null);
    }

    @Test
    public void testMedia(Context context) throws Exception {
        doTest(context, null);
    }

    @Test
    public void testMediaComplex(Context context) throws Exception {
        doTest(context, null);
    }

    @Test
    public void testKeyframes(Context context) throws Exception {
        doTest(context, null);
    }

    @Test
    public void testKeyframesPercent(Context context) throws Exception {
        doTest(context, null);
    }

    @Test
    public void testFontFace(Context context) throws Exception {
        doTest(context, null);
    }

    @Nonnull
    @Override
    protected LanguageFileType getFileType(@Nonnull Context context, @Nullable Object o) {
        return CssFileType.INSTANCE;
    }
}
