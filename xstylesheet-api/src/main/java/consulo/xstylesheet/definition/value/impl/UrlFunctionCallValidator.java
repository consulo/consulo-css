/*
 * Copyright 2013-2022 consulo.io
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

import consulo.annotation.access.RequiredReadAction;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.rawHighlight.HighlightInfo;
import consulo.language.editor.rawHighlight.HighlightInfoType;
import consulo.language.psi.PsiElement;
import consulo.util.collection.SmartList;
import consulo.xstylesheet.definition.XStyleSheetFunctionCallDescriptor;
import consulo.xstylesheet.highlight.XStyleSheetColors;
import consulo.xstylesheet.psi.PsiXStyleSheetFunctionCall;

import javax.annotation.Nonnull;
import java.util.List;

/**
* @author VISTALL
* @since 11-Sep-22
*/
@ExtensionImpl
public class UrlFunctionCallValidator implements XStyleSheetFunctionCallDescriptor
{
	@RequiredReadAction
	@Override
	public boolean isMyFunction(PsiXStyleSheetFunctionCall functionCall)
	{
		return functionCall.getCallName().equals("url");
	}

	@RequiredReadAction
	@Override
	public List<HighlightInfo> createHighlights(@Nonnull PsiXStyleSheetFunctionCall functionCall)
	{
		List<HighlightInfo> list = new SmartList<HighlightInfo>();
		list.add(HighlightInfo.newHighlightInfo(HighlightInfoType.INFORMATION).range(functionCall.getCallElement()).textAttributes(XStyleSheetColors.KEYWORD).create());

		for(PsiElement psiElement : functionCall.getParameters())
		{
			list.add(HighlightInfo.newHighlightInfo(HighlightInfoType.INFORMATION).range(psiElement).textAttributes(XStyleSheetColors.STRING).create());
		}
		return list;
	}
}
