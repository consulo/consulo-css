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

import java.util.List;

import org.jetbrains.annotations.NotNull;
import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.codeInsight.lookup.LookupElement;
import consulo.xstylesheet.definition.XStyleSheetPropertyValuePart;
import consulo.xstylesheet.definition.XStyleSheetPropertyValuePartParser;
import consulo.xstylesheet.psi.PsiXStyleSheetPropertyValuePart;

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

	@NotNull
	@Override
	public List<HighlightInfo> createHighlights(@NotNull PsiXStyleSheetPropertyValuePart valuePart)
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
	public List<LookupElement> getLookupElements()
	{
		return myParser.getLookupElements(myValue);
	}
}
