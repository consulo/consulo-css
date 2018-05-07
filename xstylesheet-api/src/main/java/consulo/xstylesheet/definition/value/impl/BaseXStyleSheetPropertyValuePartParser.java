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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.codeInsight.lookup.LookupElement;
import consulo.xstylesheet.definition.XStyleSheetPropertyValuePartParser;
import consulo.xstylesheet.psi.PsiXStyleSheetPropertyValuePart;

/**
 * @author VISTALL
 * @since 09.10.13.
 */
public class BaseXStyleSheetPropertyValuePartParser implements XStyleSheetPropertyValuePartParser
{
	@Nonnull
	@Override
	public List<HighlightInfo> createHighlights(@Nonnull PsiXStyleSheetPropertyValuePart valuePart)
	{
		return Collections.emptyList();
	}

	@Nullable
	@Override
	public Object getNativeValue(@Nonnull PsiXStyleSheetPropertyValuePart valuePart, String value)
	{
		return null;
	}

	@Nonnull
	@Override
	public List<LookupElement> getLookupElements(String value)
	{
		return Collections.emptyList();
	}
}
