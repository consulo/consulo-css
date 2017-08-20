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

package consulo.css.lang.psi.reference.nameResolving;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.psi.PsiElement;
import consulo.annotations.RequiredReadAction;
import consulo.xstylesheet.psi.XStyleSheetSelector;
import consulo.xstylesheet.psi.XStyleSheetSimpleSelector;
import consulo.xstylesheet.psi.XStyleSheetSimpleSelectorType;
import consulo.xstylesheet.psi.reference.nameResolving.XStyleRuleCondition;

/**
 * @author VISTALL
 * @since 07.07.13.
 */
public class XStyleSheetRuleTypeCondition implements XStyleRuleCondition
{
	private final XStyleSheetSimpleSelectorType myConditionType;
	private final String myName;

	public XStyleSheetRuleTypeCondition(@NotNull XStyleSheetSimpleSelectorType conditionType, @Nullable String name)
	{
		myConditionType = conditionType;
		myName = name;
	}

	@Override
	@RequiredReadAction
	public boolean isAccepted(PsiElement psiElement)
	{
		if(psiElement instanceof XStyleSheetSelector)
		{
			XStyleSheetSimpleSelector[] simpleSelectors = ((XStyleSheetSelector) psiElement).getSimpleSelectors();
			if(simpleSelectors.length == 0)
			{
				return false;
			}

			XStyleSheetSimpleSelector first = simpleSelectors[0];
			if(first.getType() != myConditionType)
			{
				return false;
			}

			return myName == null || myName.equals(first.getName());
		}

		return false;
	}
}
