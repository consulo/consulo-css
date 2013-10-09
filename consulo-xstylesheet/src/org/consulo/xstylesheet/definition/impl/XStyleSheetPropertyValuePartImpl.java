package org.consulo.xstylesheet.definition.impl;

import java.util.List;

import org.consulo.xstylesheet.definition.XStyleSheetPropertyValuePart;
import org.consulo.xstylesheet.definition.XStyleSheetPropertyValuePartParser;
import org.consulo.xstylesheet.psi.PsiXStyleSheetPropertyValuePart;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.codeInsight.lookup.LookupElement;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class XStyleSheetPropertyValuePartImpl implements XStyleSheetPropertyValuePart
{
	private final XStyleSheetPropertyValuePartParser myParser;
	private final String myValue;

	public XStyleSheetPropertyValuePartImpl(XStyleSheetPropertyValuePartParser parser, String value)
	{
		myParser = parser;
		myValue = value;
	}

	@Override
	public XStyleSheetPropertyValuePartParser getParser()
	{
		return myParser;
	}

	@Nullable
	@Override
	public HighlightInfo createHighlightInfo(@NotNull PsiXStyleSheetPropertyValuePart valuePart)
	{
		return myParser.createHighlightInfo(valuePart);
	}

	@Override
	public String getValue()
	{
		return myValue;
	}

	@Override
	public Object getNativeValue(PsiXStyleSheetPropertyValuePart part)
	{
		return myParser.getNativeValue(part, myValue);
	}

	@Override
	public List<LookupElement> getLookupElements()
	{
		return myParser.getLookupElements(myValue);
	}
}
