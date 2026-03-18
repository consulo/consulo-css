package consulo.scss.parsing;

import consulo.language.file.LanguageFileType;
import consulo.scss.lang.ScssFileType;
import consulo.test.junit.impl.language.SimpleParsingTest;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.junit.jupiter.api.Test;

/**
 * @author VISTALL
 * @since 2026-03-18
 */
public class ScssParsingTest extends SimpleParsingTest<Object> {
    public ScssParsingTest() {
        super("parsing", "scss");
    }

    @Test
    public void testVariableDeclaration(Context context) throws Exception {
        doTest(context, null);
    }

    @Test
    public void testVariableReference(Context context) throws Exception {
        doTest(context, null);
    }

    @Test
    public void testNesting(Context context) throws Exception {
        doTest(context, null);
    }

    @Test
    public void testParentSelector(Context context) throws Exception {
        doTest(context, null);
    }

    @Test
    public void testMixin(Context context) throws Exception {
        doTest(context, null);
    }

    @Test
    public void testInclude(Context context) throws Exception {
        doTest(context, null);
    }

    @Test
    public void testUseForward(Context context) throws Exception {
        doTest(context, null);
    }

    @Test
    public void testLineComment(Context context) throws Exception {
        doTest(context, null);
    }

    @Nonnull
    @Override
    protected LanguageFileType getFileType(@Nonnull Context context, @Nullable Object o) {
        return ScssFileType.INSTANCE;
    }
}
