package org.consulo.xstylesheet;

import com.intellij.icons.AllIcons;
import com.intellij.ide.IconProvider;
import com.intellij.openapi.util.Iconable;
import com.intellij.psi.PsiElement;
import org.consulo.xstylesheet.psi.PsiXStyleSheetSelectorDeclaration;
import org.consulo.xstylesheet.psi.PsiXStyleSheetSelectorReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author VISTALL
 * @since 08.07.13.
 */
public class XStyleSheetIconProvider extends IconProvider {
	@Nullable
	@Override
	public Icon getIcon(@NotNull PsiElement psiElement, @Iconable.IconFlags int i) {
		if (psiElement instanceof PsiXStyleSheetSelectorDeclaration) {
			PsiXStyleSheetSelectorReference[] selectorReferences = ((PsiXStyleSheetSelectorDeclaration) psiElement).getSelectorReferences();

			if(selectorReferences.length == 0) {
				return null;
			}

			PsiXStyleSheetSelectorReference selectorReference = selectorReferences[0];
			if (selectorReference.isClassRule()) {
				return XStyleSheetIcons.CssClass;
			} else if (selectorReference.isIdRule()) {
				return XStyleSheetIcons.HtmlId;
			} else {
				return AllIcons.Nodes.Tag;
			}
		}
		return null;
	}
}
