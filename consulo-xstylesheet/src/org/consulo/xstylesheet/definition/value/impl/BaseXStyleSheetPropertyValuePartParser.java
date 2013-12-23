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

package org.consulo.xstylesheet.definition.value.impl;

import java.util.Collections;
import java.util.List;

import org.consulo.xstylesheet.definition.XStyleSheetPropertyValuePartParser;
import org.consulo.xstylesheet.psi.PsiXStyleSheetPropertyValuePart;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.codeInsight.lookup.LookupElement;

/**
 * @author VISTALL
 * @since 09.10.13.
 */
public class BaseXStyleSheetPropertyValuePartParser implements XStyleSheetPropertyValuePartParser
{
	@NotNull
	@Override
	public List<HighlightInfo> createHighlights(@NotNull PsiXStyleSheetPropertyValuePart valuePart)
	{
		return Collections.emptyList();
	}

	@Nullable
	@Override
	public Object getNativeValue(@NotNull PsiXStyleSheetPropertyValuePart valuePart, String value)
	{
		return null;
	}

	@NotNull
	@Override
	public List<LookupElement> getLookupElements(String value)
	{
		return Collections.emptyList();
	}
}
