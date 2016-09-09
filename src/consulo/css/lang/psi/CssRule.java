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
import org.jetbrains.annotations.Nullable;
import com.intellij.lang.ASTNode;
import consulo.xstylesheet.psi.PsiXStyleSheetRule;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class CssRule extends CssElement implements PsiXStyleSheetRule
{
	public CssRule(@NotNull ASTNode node)
	{
		super(node);
	}

	@Nullable
	@Override
	public CssBlock getBlock()
	{
		return findChildByClass(CssBlock.class);
	}

	@NotNull
	@Override
	public CssProperty[] getProperties()
	{
		CssBlock block = getBlock();
		return block == null ? CssProperty.EMPTY_ARRAY : block.getProperties();
	}

	@Nullable
	@Override
	public CssSelectorDeclarationList getSelectorDeclarationList()
	{
		return findChildByClass(CssSelectorDeclarationList.class);
	}

	@Override
	@NotNull
	public CssSelectorDeclaration[] getSelectorDeclarations()
	{
		CssSelectorDeclarationList selectorReferenceList = getSelectorDeclarationList();
		return selectorReferenceList == null ? CssSelectorDeclaration.EMPTY_ARRAY : selectorReferenceList.getSelectorDeclarations();
	}
}
