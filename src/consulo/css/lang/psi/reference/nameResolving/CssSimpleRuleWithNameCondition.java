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

import com.intellij.psi.PsiElement;
import consulo.css.lang.psi.CssSelectorDeclaration;
import consulo.css.lang.psi.CssSelectorReference;
import consulo.xstylesheet.psi.reference.nameResolving.XStyleRuleCondition;

/**
 * @author VISTALL
 * @since 07.07.13.
 */
public class CssSimpleRuleWithNameCondition implements XStyleRuleCondition
{
	private CssSimpleRuleConditionType myConditionType;
	private String myName;

	public CssSimpleRuleWithNameCondition(CssSimpleRuleConditionType conditionType, String name)
	{
		myConditionType = conditionType;
		myName = name;
	}

	@Override
	public boolean isAccepted(PsiElement psiElement)
	{
		if(psiElement instanceof CssSelectorReference)
		{
			CssSelectorReference selectorReference = (CssSelectorReference) psiElement;

			if(!myName.equals(selectorReference.getName()))
			{
				return false;
			}

			switch(myConditionType)
			{
				case ID:
					return selectorReference.isIdRule();
				case CLASS:
					return selectorReference.isClassRule();
			}
		}

		if(psiElement instanceof CssSelectorDeclaration)
		{
			CssSelectorReference[] selectorReferences = ((CssSelectorDeclaration) psiElement).getSelectorReferences();
			for(CssSelectorReference selectorReference : selectorReferences)
			{
				if(isAccepted(selectorReference))
				{
					return true;
				}
			}
		}

		return false;
	}

	public CssSimpleRuleConditionType getConditionType()
	{
		return myConditionType;
	}
}
