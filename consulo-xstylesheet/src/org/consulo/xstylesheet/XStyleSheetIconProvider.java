package org.consulo.xstylesheet;

import com.intellij.icons.AllIcons;
import com.intellij.ide.IconDescriptor;
import com.intellij.ide.IconDescriptorUpdater;
import com.intellij.psi.PsiElement;
import org.consulo.xstylesheet.psi.PsiXStyleSheetSelectorDeclaration;
import org.consulo.xstylesheet.psi.PsiXStyleSheetSelectorReference;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 08.07.13.
 */
public class XStyleSheetIconProvider implements IconDescriptorUpdater
{
	@Override
	public void updateIcon(@NotNull IconDescriptor iconDescriptor, @NotNull PsiElement element, int i)
	{
		if (element instanceof PsiXStyleSheetSelectorDeclaration)
		{
			PsiXStyleSheetSelectorReference[] selectorReferences = ((PsiXStyleSheetSelectorDeclaration) element).getSelectorReferences();

			if (selectorReferences.length == 0)
			{
				return;
			}

			PsiXStyleSheetSelectorReference selectorReference = selectorReferences[0];
			if (selectorReference.isClassRule())
			{
				iconDescriptor.setMainIcon(XStyleSheetIcons.CssClass);
			}
			else if (selectorReference.isIdRule())
			{
				iconDescriptor.setMainIcon(XStyleSheetIcons.HtmlId);
			} else
			{
				iconDescriptor.setMainIcon(AllIcons.Nodes.Tag);
			}
		}
	}
}
