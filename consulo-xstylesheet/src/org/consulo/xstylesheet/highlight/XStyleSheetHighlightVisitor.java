package org.consulo.xstylesheet.highlight;

import org.consulo.xstylesheet.definition.XStyleSheetPropertyValuePart;
import org.consulo.xstylesheet.psi.PsiXStyleSheetProperty;
import org.consulo.xstylesheet.psi.PsiXStyleSheetPropertyValuePart;
import org.consulo.xstylesheet.psi.PsiXStyleSheetSelectorAttribute;
import org.consulo.xstylesheet.psi.PsiXStyleSheetSelectorPseudoClass;
import org.consulo.xstylesheet.psi.PsiXStyleSheetSelectorReference;
import org.jetbrains.annotations.NotNull;
import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.codeInsight.daemon.impl.HighlightInfoType;
import com.intellij.codeInsight.daemon.impl.HighlightVisitor;
import com.intellij.codeInsight.daemon.impl.analysis.HighlightInfoHolder;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNameIdentifierOwner;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class XStyleSheetHighlightVisitor implements HighlightVisitor, XStyleSheetColors
{
	private HighlightInfoHolder myHighlightInfoHolder;

	@Override
	public boolean suitableForFile(@NotNull PsiFile psiFile)
	{
		return true;
	}

	@Override
	public void visit(@NotNull PsiElement psiElement)
	{
		psiElement.accept(new PsiElementVisitor()
		{
			@Override
			public void visitElement(PsiElement element)
			{
				if(element instanceof PsiXStyleSheetProperty)
				{
					highlightName((PsiNameIdentifierOwner) element, PROPERTY_NAME);
				}
				else if(element instanceof PsiXStyleSheetPropertyValuePart)
				{
					XStyleSheetPropertyValuePart valuePart = ((PsiXStyleSheetPropertyValuePart) element).getValuePart();
					if(valuePart != null)
					{
						myHighlightInfoHolder.addAll(valuePart.createHighlights((PsiXStyleSheetPropertyValuePart) element));
					}
				}
				else if(element instanceof PsiXStyleSheetSelectorAttribute)
				{
					highlightName((PsiNameIdentifierOwner) element, ATTRIBUTE_NAME);
				}
				else if(element instanceof PsiXStyleSheetSelectorReference)
				{
					TextAttributesKey key = null;
					if(((PsiXStyleSheetSelectorReference) element).isClassRule())
					{
						key = XStyleSheetColors.SELECTOR_CLASS_NAME;
					}
					else if(((PsiXStyleSheetSelectorReference) element).isIdRule())
					{
						key = XStyleSheetColors.SELECTOR_ID_NAME;
					}
					else
					{
						key = XStyleSheetColors.SELECTOR_ELEMENT_NAME;
					}

					highlightName((PsiNameIdentifierOwner) element, key);
				}
				else if(element instanceof PsiXStyleSheetSelectorPseudoClass)
				{
					highlightName((PsiNameIdentifierOwner) element, PSEUDO_NAME);
				}

				element.acceptChildren(this);
			}
		});
	}

	private void highlightName(PsiNameIdentifierOwner owner, TextAttributesKey key)
	{
		PsiElement nameIdentifier = owner.getNameIdentifier();
		if(nameIdentifier != null)
		{
			HighlightInfo.Builder builder = HighlightInfo.newHighlightInfo(HighlightInfoType.INFORMATION);
			builder.textAttributes(key);
			builder.range(nameIdentifier);

			myHighlightInfoHolder.add(builder.create());
		}
	}

	@Override
	public boolean analyze(@NotNull PsiFile psiFile, boolean b, @NotNull HighlightInfoHolder highlightInfoHolder, @NotNull Runnable runnable)
	{
		myHighlightInfoHolder = highlightInfoHolder;
		runnable.run();
		return true;
	}

	@NotNull
	@Override
	public HighlightVisitor clone()
	{
		return new XStyleSheetHighlightVisitor();
	}

	@Override
	public int order()
	{
		return 0;
	}
}
