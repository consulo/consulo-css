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

import org.consulo.xstylesheet.highlight.XStyleSheetColors;
import org.consulo.xstylesheet.psi.PsiXStyleSheetFunctionCall;
import org.consulo.xstylesheet.psi.PsiXStyleSheetPropertyValuePart;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.codeInsight.daemon.impl.HighlightInfoType;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.psi.PsiElement;
import com.intellij.util.SmartList;

/**
 * @author VISTALL
 * @since 09.10.13.
 */
public class FunctionXStyleSheetValue extends BaseXStyleSheetPropertyValuePartParser
{
	public static final ExtensionPointName<XStyleSheetFunctionCallDescriptor> EP_NAME = ExtensionPointName.create("consulo.xstylesheet.functionCallValidator");


	public static interface XStyleSheetFunctionCallDescriptor
	{
		boolean isMyFunction(PsiXStyleSheetFunctionCall functionCall);

		List<HighlightInfo> createHighlights(@NotNull PsiXStyleSheetFunctionCall functionCall);
	}

	public static class UrlFunctionCallValidator implements XStyleSheetFunctionCallDescriptor
	{
		@Override
		public boolean isMyFunction(PsiXStyleSheetFunctionCall functionCall)
		{
			return functionCall.getName().equals("url");
		}

		@Override
		public List<HighlightInfo> createHighlights(@NotNull PsiXStyleSheetFunctionCall functionCall)
		{
			List<HighlightInfo> list = new SmartList<HighlightInfo>();
			list.add(HighlightInfo.newHighlightInfo(HighlightInfoType.INFORMATION).range(functionCall.getNameIdentifier()).textAttributes(XStyleSheetColors.KEYWORD).create());

			for(PsiElement psiElement : functionCall.getParameters())
			{
				list.add(HighlightInfo.newHighlightInfo(HighlightInfoType.INFORMATION).range(psiElement).textAttributes(XStyleSheetColors.STRING).create());
			}
			return list;
		}
	}

	@NotNull
	@Override
	public List<HighlightInfo> createHighlights(@NotNull PsiXStyleSheetPropertyValuePart valuePart)
	{
		Object nativeValue = getNativeValue(valuePart, null);
		if(nativeValue instanceof XStyleSheetFunctionCallDescriptor)
		{
			return ((XStyleSheetFunctionCallDescriptor) nativeValue).createHighlights((PsiXStyleSheetFunctionCall) valuePart.getFirstChild());
		}
		return Collections.emptyList();
	}

	@Nullable
	@Override
	public Object getNativeValue(@NotNull PsiXStyleSheetPropertyValuePart valuePart, String value)
	{
		PsiElement firstChild = valuePart.getFirstChild();
		if(firstChild instanceof PsiXStyleSheetFunctionCall)
		{
			for(XStyleSheetFunctionCallDescriptor descriptor : EP_NAME.getExtensions())
			{
				if(descriptor.isMyFunction((PsiXStyleSheetFunctionCall) firstChild))
				{
					return descriptor;
				}
			}
		}
		return null;
	}

	@NotNull
	@Override
	public List<LookupElement> getLookupElements(String value)
	{
		return Collections.emptyList();
	}
}
