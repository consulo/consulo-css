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

package consulo.xstylesheet.definition.value.impl;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;

/**
 * @author VISTALL
 * @since 08.07.13.
 */
public class BaseNumberXStyleSheetValue extends TextBasedXStyleSheetPropertyValuePartParser
{
	private String[] myPrefixes;

	public BaseNumberXStyleSheetValue(@NotNull String... prefixes)
	{
		myPrefixes = prefixes;
	}

	@Nullable
	@Override
	public Object fromString(@NotNull String stringValue, String value)
	{
		if(value != null && !stringValue.equals(value))
		{
			return null;
		}

		String prefix = null;
		for(String t : myPrefixes)
		{
			if(stringValue.endsWith(t))
			{
				prefix = t;
				break;
			}
		}

		if(prefix == null && myPrefixes.length > 0)
		{
			return null;
		}
		else if(prefix != null)
		{
			stringValue = stringValue.substring(0, stringValue.length() - prefix.length());
		}

		try
		{
			return Integer.parseInt(stringValue);
		}
		catch(NumberFormatException e)
		{
			//
		}
		return null;
	}

	@NotNull
	@Override
	public List<LookupElement> getLookupElements(String value)
	{
		LookupElementBuilder builder = LookupElementBuilder.create(value);
		return Collections.<LookupElement>singletonList(builder);
	}
}
