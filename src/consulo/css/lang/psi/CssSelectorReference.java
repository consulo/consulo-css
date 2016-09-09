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

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.util.IncorrectOperationException;
import consulo.css.lang.CssTokens;
import consulo.xstylesheet.psi.PsiXStyleSheetSelectorReference;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class CssSelectorReference extends CssElement implements PsiXStyleSheetSelectorReference, PsiNameIdentifierOwner
{
	public static final CssSelectorReference[] EMPTY_ARRAY = new CssSelectorReference[0];

	public CssSelectorReference(@NotNull ASTNode node)
	{
		super(node);
	}

	@Override
	public boolean isClassRule()
	{
		return getFirstChild().getNode().getElementType() == CssTokens.DOT;
	}

	@Override
	public boolean isIdRule()
	{
		return getFirstChild().getNode().getElementType() == CssTokens.SHARP;
	}

	@Nullable
	@Override
	public PsiElement getNameIdentifier()
	{
		return findChildByType(CssTokens.IDENTIFIER);
	}

	@Override
	public String getName()
	{
		PsiElement nameIdentifier = getNameIdentifier();
		return nameIdentifier == null ? getText() : nameIdentifier.getText();
	}

	@Override
	public PsiElement setName(@NonNls @NotNull String s) throws IncorrectOperationException
	{
		return null;
	}
}
