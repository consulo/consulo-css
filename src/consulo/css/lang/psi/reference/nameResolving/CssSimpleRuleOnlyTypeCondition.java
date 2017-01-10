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
import consulo.css.lang.psi.CssSelectorReference;
import consulo.xstylesheet.psi.reference.nameResolving.XStyleRuleCondition;

/**
 * @author VISTALL
 * @since 07.07.13.
 */
public class CssSimpleRuleOnlyTypeCondition implements XStyleRuleCondition
{
	private CssSimpleRuleConditionType myConditionType;


	public CssSimpleRuleOnlyTypeCondition(CssSimpleRuleConditionType conditionType)
	{
		myConditionType = conditionType;
	}

	@Override
	public boolean isAccepted(PsiElement psiElement)
	{
		if(psiElement instanceof CssSelectorReference)
		{
			CssSelectorReference selectorReference = (CssSelectorReference) psiElement;

			switch(myConditionType)
			{
				case ID:
					return selectorReference.isIdRule();
				case CLASS:
					return selectorReference.isClassRule();
			}
		}

		return false;
	}
}