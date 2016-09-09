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

package consulo.css.editor.structureView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.structureView.impl.common.PsiTreeElementBase;
import consulo.css.lang.psi.CssFile;
import consulo.css.lang.psi.CssRule;
import consulo.css.lang.psi.CssSelectorDeclaration;

/**
 * @author VISTALL
 * @since 08.07.13.
 */
public class CssStructureViewTreeElement extends PsiTreeElementBase<CssFile>
{

	public CssStructureViewTreeElement(CssFile psiElement)
	{
		super(psiElement);
	}

	@NotNull
	@Override
	public Collection<StructureViewTreeElement> getChildrenBase()
	{
		List<StructureViewTreeElement> list = new ArrayList<StructureViewTreeElement>();
		for(CssRule cssRule : getElement().getRules())
		{
			for(CssSelectorDeclaration declaration : cssRule.getSelectorDeclarations())
			{
				list.add(new CssSelectorDeclarationStructureViewTreeElement(declaration));
			}
		}
		return list;
	}

	@Nullable
	@Override
	public String getPresentableText()
	{
		return getElement().getName();
	}
}
