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

package consulo.xstylesheet.codeInsight;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import consulo.xstylesheet.definition.XStyleSheetProperty;
import consulo.xstylesheet.psi.PsiXStyleSheetProperty;
import consulo.xstylesheet.psi.PsiXStyleSheetPropertyValuePart;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 08.07.13.
 */
public class PropertyValueIsNotValidInspection extends LocalInspectionTool
{
	@Nonnull
	@Override
	public com.intellij.psi.PsiElementVisitor buildVisitor(@Nonnull final ProblemsHolder holder, boolean isOnTheFly)
	{
		return new PsiElementVisitor()
		{
			@Override
			public void visitElement(PsiElement element)
			{
				if(element instanceof PsiXStyleSheetProperty)
				{
					XStyleSheetProperty property = ((PsiXStyleSheetProperty) element).getXStyleSheetProperty();
					if(property == null || property.isUnknown())
					{
						return;
					}

					for(PsiXStyleSheetPropertyValuePart xStyleSheetPropertyValuePart : ((PsiXStyleSheetProperty) element).getParts())
					{
						Object value = xStyleSheetPropertyValuePart.getValue();
						if(value == null)
						{
							holder.registerProblem(xStyleSheetPropertyValuePart, "Invalid property value", ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
						}
					}
				}
			}
		};
	}
}
