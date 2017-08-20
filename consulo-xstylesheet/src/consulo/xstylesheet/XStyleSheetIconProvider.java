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

package consulo.xstylesheet;

import org.jetbrains.annotations.NotNull;
import com.intellij.icons.AllIcons;
import com.intellij.psi.PsiElement;
import consulo.annotations.RequiredReadAction;
import consulo.ide.IconDescriptor;
import consulo.ide.IconDescriptorUpdater;
import consulo.xstylesheet.psi.XStyleSheetSimpleSelector;

/**
 * @author VISTALL
 * @since 08.07.13.
 */
public class XStyleSheetIconProvider implements IconDescriptorUpdater
{
	@RequiredReadAction
	@Override
	public void updateIcon(@NotNull IconDescriptor iconDescriptor, @NotNull PsiElement element, int i)
	{
		if(element instanceof XStyleSheetSimpleSelector)
		{
			switch(((XStyleSheetSimpleSelector) element).getType())
			{
				case ID:
					iconDescriptor.setMainIcon(XStyleSheetIcons.HtmlId);
					break;
				case CLASS:
					iconDescriptor.setMainIcon(XStyleSheetIcons.CssClass);
					break;
				case TAG:
					iconDescriptor.setMainIcon(AllIcons.Nodes.Tag);
					break;
			}
		}
	}
}
