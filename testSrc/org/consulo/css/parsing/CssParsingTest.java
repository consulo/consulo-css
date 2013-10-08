package org.consulo.css.parsing;

import org.consulo.css.lang.parser.CssParserDefinition;
import com.intellij.testFramework.ParsingTestCase;

/**
 * @author VISTALL
 * @since 02.07.13.
 */
public class CssParsingTest extends ParsingTestCase
{
	public CssParsingTest()
	{
		super("parsing", "css", new CssParserDefinition());
	}

	public void testCssParsing1()
	{
		doTest(true);
	}

	public void testCssParsingSelectorList()
	{
		doTest(true);
	}

	public void testAsterisk()
	{
		doTest(true);
	}

	public void testMultiSelectors()
	{
		doTest(true);
	}

	public void testSelectorAttribute()
	{
		doTest(true);
	}

	public void testWiki()
	{
		doTest(true);
	}

	public void testUrl()
	{
		doTest(true);
	}

	@Override
	protected String getTestDataPath()
	{
		return "testData";
	}

	@Override
	protected boolean shouldContainTempFiles()
	{
		return false;
	}
}
