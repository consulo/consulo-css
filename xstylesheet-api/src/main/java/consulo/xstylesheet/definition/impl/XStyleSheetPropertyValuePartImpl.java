/*
 * Copyright 2013 must-be.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package consulo.xstylesheet.definition.impl;

import consulo.language.editor.completion.lookup.LookupElement;
import consulo.language.editor.rawHighlight.HighlightInfo;
import consulo.xstylesheet.definition.XStyleSheetPropertyValuePart;
import consulo.xstylesheet.definition.XStyleSheetPropertyValuePartParser;
import consulo.xstylesheet.psi.PsiXStyleSheetPropertyValuePart;

import javax.annotation.Nonnull;
import java.util.List;

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

	@Nonnull
	@Override
	public List<HighlightInfo> createHighlights(@Nonnull PsiXStyleSheetPropertyValuePart valuePart)
	{
		return myParser.createHighlights(valuePart);
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
	public boolean setNativeValue(@Nonnull XStyleSheetPropertyValuePart part, Object value)
	{
		return false;
	}

	@Override
	public List<LookupElement> getLookupElements()
	{
		return myParser.getLookupElements(myValue);
	}
}
