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

package consulo.xstylesheet.psi.reference.nameResolving;

import consulo.annotation.access.RequiredReadAction;
import consulo.language.psi.PsiElement;
import consulo.xstylesheet.psi.XStyleSheetSelector;
import consulo.xstylesheet.psi.XStyleSheetSimpleSelector;
import consulo.xstylesheet.psi.XStyleSheetSimpleSelectorType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author VISTALL
 * @since 2013-07-07
 */
public class XStyleSheetRuleTypeCondition implements XStyleRuleCondition {
    private final XStyleSheetSimpleSelectorType myConditionType;
    private final String myName;

    public XStyleSheetRuleTypeCondition(@Nonnull XStyleSheetSimpleSelectorType conditionType, @Nullable String name) {
        myConditionType = conditionType;
        myName = name;
    }

    @Override
    @RequiredReadAction
    public boolean isAccepted(PsiElement psiElement) {
        if (psiElement instanceof XStyleSheetSelector selector) {
            XStyleSheetSimpleSelector[] simpleSelectors = selector.getSimpleSelectors();
            if (simpleSelectors.length == 0) {
                return false;
            }

            XStyleSheetSimpleSelector first = simpleSelectors[0];
            return first.getType() == myConditionType && (myName == null || myName.equals(first.getName()));
        }

        return false;
    }
}
