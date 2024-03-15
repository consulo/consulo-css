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

import consulo.annotation.access.RequiredReadAction;
import consulo.annotation.component.ExtensionImpl;
import consulo.application.AllIcons;
import consulo.language.icon.IconDescriptor;
import consulo.language.icon.IconDescriptorUpdater;
import consulo.language.psi.PsiElement;
import consulo.platform.base.icon.PlatformIconGroup;
import consulo.xstylesheet.icon.XStyleSheetIconGroup;
import consulo.xstylesheet.psi.PsiXStyleSheetProperty;
import consulo.xstylesheet.psi.PsiXStyleSheetVariable;
import consulo.xstylesheet.psi.XStyleSheetSelector;
import consulo.xstylesheet.psi.XStyleSheetSimpleSelector;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 08.07.13.
 */
@ExtensionImpl
public class XStyleSheetIconProvider implements IconDescriptorUpdater
{
	@RequiredReadAction
	@Override
	public void updateIcon(@Nonnull IconDescriptor iconDescriptor, @Nonnull PsiElement element, int i)
	{
		if(element instanceof PsiXStyleSheetVariable)
		{
			iconDescriptor.setMainIcon(PlatformIconGroup.nodesVariable());
		}
		else if(element instanceof PsiXStyleSheetProperty)
		{
			iconDescriptor.setMainIcon(PlatformIconGroup.nodesProperty());
		}

		if(element instanceof XStyleSheetSelector)
		{
			XStyleSheetSimpleSelector[] simpleSelectors = ((XStyleSheetSelector) element).getSimpleSelectors();

			if(simpleSelectors.length == 0)
			{
				return;
			}

			XStyleSheetSimpleSelector selector = simpleSelectors[0];
			switch(selector.getType())
			{
				case ID:
					iconDescriptor.setMainIcon(XStyleSheetIconGroup.html_id());
					break;
				case CLASS:
					iconDescriptor.setMainIcon(XStyleSheetIconGroup.css_class());
					break;
				case TAG:
					iconDescriptor.setMainIcon(AllIcons.Nodes.Tag);
					break;
			}
		}
	}
}
