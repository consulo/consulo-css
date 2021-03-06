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

import java.util.Collection;
import java.util.Collections;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.structureView.impl.common.PsiTreeElementBase;
import consulo.xstylesheet.psi.XStyleSheetSelector;

/**
 * @author VISTALL
 * @since 08.07.13.
 */
public class CssSelectorDeclarationStructureViewTreeElement extends PsiTreeElementBase<XStyleSheetSelector>
{
	public CssSelectorDeclarationStructureViewTreeElement(XStyleSheetSelector psiElement)
	{
		super(psiElement);
	}

	@Nonnull
	@Override
	public Collection<StructureViewTreeElement> getChildrenBase()
	{
		return Collections.emptyList();
	}

	@Nullable
	@Override
	public String getPresentableText()
	{
		return getElement().getText();
	}
}
