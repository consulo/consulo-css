package org.consulo.css.parsing;

import com.intellij.lang.ParserDefinition;
import com.intellij.testFramework.ParsingTestCase;
import org.consulo.css.lang.CssParserDefinition;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 02.07.13.
 */
public class CssParsingTest extends ParsingTestCase {
    public CssParsingTest() {
        super("parsing", "css", new CssParserDefinition());
    }

    public void testCssParsing1() {
        doTest(true);
    }

    @Override
    protected String getTestDataPath() {
        return "testData";
    }

    @Override
    protected boolean shouldContainTempFiles() {
        return false;
    }
}
