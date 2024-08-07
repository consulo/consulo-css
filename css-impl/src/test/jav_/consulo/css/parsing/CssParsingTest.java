package consulo.css.parsing;

import consulo.testFramework.ParsingTestCase;

/**
 * @author VISTALL
 * @since 2013-07-02
 */
public abstract class CssParsingTest extends ParsingTestCase {
    public CssParsingTest() {
        super("parsing", "css");
    }

    public void testCssParsing1() {
        doTest(true);
    }

    public void testCssParsingSelectorList() {
        doTest(true);
    }

    public void testAsterisk() {
        doTest(true);
    }

    public void testMultiSelectors() {
        doTest(true);
    }

    public void testSelectorAttribute() {
        doTest(true);
    }

    public void testWiki() {
        doTest(true);
    }

    public void testUrl() {
        doTest(true);
    }

    @Override
    protected String getTestDataPath() {
        return "/testData";
    }

    @Override
    protected boolean shouldContainTempFiles() {
        return false;
    }
}
