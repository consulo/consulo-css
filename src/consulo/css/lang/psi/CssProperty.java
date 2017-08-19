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
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.util.IncorrectOperationException;
import consulo.css.lang.CssTokens;
import consulo.xstylesheet.definition.XStyleSheetProperty;
import consulo.xstylesheet.definition.XStyleSheetTable;
import consulo.xstylesheet.psi.PsiXStyleSheetProperty;
import consulo.xstylesheet.psi.PsiXStyleSheetPropertyValuePart;
import consulo.xstylesheet.psi.XStyleSheetFile;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class CssProperty extends CssElement implements PsiNameIdentifierOwner, PsiXStyleSheetProperty
{
	public static final CssProperty[] EMPTY_ARRAY = new CssProperty[0];

	public CssProperty(@NotNull ASTNode node)
	{
		super(node);
	}

	@Nullable
	@Override
	public PsiElement getNameIdentifier()
	{
		PsiElement firstChild = getFirstChild();
		if(firstChild.getNode().getElementType() == CssTokens.IDENTIFIER)
		{
			return firstChild;
		}
		return null;
	}

	@Nullable
	@Override
	public XStyleSheetProperty getXStyleSheetProperty()
	{
		String name = getName();
		if(name == null)
		{
			return null;
		}
		PsiFile containingFile = getContainingFile();
		XStyleSheetTable xStyleSheetTable = XStyleSheetFile.getXStyleSheetTable(containingFile);
		return xStyleSheetTable.findProperty(name);
	}

	@NotNull
	@Override
	public PsiXStyleSheetPropertyValuePart[] getParts()
	{
		return findChildrenByClass(PsiXStyleSheetPropertyValuePart.class);
	}

	@Override
	public String getName()
	{
		PsiElement nameIdentifier = getNameIdentifier();
		return nameIdentifier == null ? null : nameIdentifier.getText();
	}

	@Override
	public PsiElement setName(@NonNls @NotNull String s) throws IncorrectOperationException
	{
		return null;
	}
}
