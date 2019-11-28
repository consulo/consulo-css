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

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceBase;
import consulo.annotation.access.RequiredReadAction;
import consulo.css.lang.CssTokens;
import consulo.xstylesheet.psi.PsiXStyleSheetFunctionCall;
import consulo.xstylesheet.psi.PsiXStyleSheetFunctionCallParameterList;
import consulo.xstylesheet.psi.reference.impl.BuildInSymbolElement;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author VISTALL
 * @since 08.10.13.
 */
public class CssFunctionCall extends CssElement implements PsiXStyleSheetFunctionCall
{
	private static class Ref extends PsiReferenceBase<PsiElement>
	{
		@RequiredReadAction
		public Ref(@Nonnull CssFunctionCall element)
		{
			super(element, new TextRange(0, element.getCallElement().getTextLength()));
		}

		@RequiredReadAction
		@Nullable
		@Override
		public PsiElement resolve()
		{
			return new BuildInSymbolElement(getElement());
		}
	}

	public CssFunctionCall(@Nonnull ASTNode node)
	{
		super(node);
	}

	@Override
	@RequiredReadAction
	public PsiReference getReference()
	{
		return new Ref(this);
	}

	@Nonnull
	@Override
	@RequiredReadAction
	public PsiElement getCallElement()
	{
		return findNotNullChildByType(CssTokens.IDENTIFIER);
	}

	@Nonnull
	@RequiredReadAction
	@Override
	public String getCallName()
	{
		PsiElement nameIdentifier = getCallElement();

		return nameIdentifier.getText();
	}

	@RequiredReadAction
	@Override
	public PsiXStyleSheetFunctionCallParameterList getParameterList()
	{
		return findChildByClass(PsiXStyleSheetFunctionCallParameterList.class);
	}

	@RequiredReadAction
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
