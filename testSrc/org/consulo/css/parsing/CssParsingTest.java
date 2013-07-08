package org.consulo.css.parsing;

import com.intellij.testFramework.ParsingTestCase;
import org.consulo.css.lang.parser.CssParserDefinition;

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

  public void testCssParsingSelectorList() {
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
