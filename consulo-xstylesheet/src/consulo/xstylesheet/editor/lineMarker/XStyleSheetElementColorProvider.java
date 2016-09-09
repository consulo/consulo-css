/*
 * Copyright 2013-2014 must-be.org
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

package consulo.xstylesheet.editor.lineMarker;

import java.awt.Color;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.editor.ElementColorProvider;
import com.intellij.psi.PsiElement;
import consulo.xstylesheet.psi.PsiXStyleSheetPropertyValuePart;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class XStyleSheetElementColorProvider implements ElementColorProvider
{
	@Nullable
	@Override
	public Color getColorFrom(@NotNull PsiElement psiElement)
	{
		if(psiElement instanceof PsiXStyleSheetPropertyValuePart)
		{
			Object value = null;
			try
			{
				value = ((PsiXStyleSheetPropertyValuePart) psiElement).getValue();
			}
			catch(Exception e)
			{
				System.out.println(psiElement.getText());
				e.printStackTrace();
			}
			if(value instanceof Color)
			{
				return (Color) value;
			}
		}
		return null;
	}

	@Override
	public void setColorTo(@NotNull PsiElement psiElement, @NotNull Color color)
	{
		//TODO [VISTALL] !
	}
}
