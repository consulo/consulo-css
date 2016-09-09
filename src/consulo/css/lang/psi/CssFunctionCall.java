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

package consulo.css.lang.psi;

import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import consulo.css.lang.CssTokens;
import consulo.xstylesheet.psi.PsiXStyleSheetFunctionCall;
import consulo.xstylesheet.psi.PsiXStyleSheetFunctionCallParameterList;

/**
 * @author VISTALL
 * @since 08.10.13.
 */
public class CssFunctionCall extends CssElement implements PsiXStyleSheetFunctionCall
{
	public CssFunctionCall(@NotNull ASTNode node)
	{
		super(node);
	}

	@Override
	public PsiElement getNameIdentifier()
	{
		return findNotNullChildByType(CssTokens.FUNCTION_NAME);
	}

	@Override
	public String getName()
	{
		PsiElement nameIdentifier = getNameIdentifier();

		return nameIdentifier.getText();
	}

	@Override
	public PsiXStyleSheetFunctionCallParameterList getParameterList()
	{
		return findChildByClass(PsiXStyleSheetFunctionCallParameterList.class);
	}

	@Override
	public PsiElement[] getParameters()
	{
		PsiXStyleSheetFunctionCallParameterList parameterList = getParameterList();
		if(parameterList == null)
		{
			return PsiElement.EMPTY_ARRAY;
		}
		return parameterList.getParameters();
	}
}
