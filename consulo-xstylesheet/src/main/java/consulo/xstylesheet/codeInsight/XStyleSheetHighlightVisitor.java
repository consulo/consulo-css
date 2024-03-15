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

package consulo.xstylesheet.codeInsight;

import consulo.annotation.access.RequiredReadAction;
import consulo.colorScheme.TextAttributesKey;
import consulo.language.editor.rawHighlight.HighlightInfo;
import consulo.language.editor.rawHighlight.HighlightInfoHolder;
import consulo.language.editor.rawHighlight.HighlightInfoType;
import consulo.language.editor.rawHighlight.HighlightVisitor;
import consulo.language.psi.*;
import consulo.xstylesheet.definition.XStyleSheetPropertyValuePart;
import consulo.xstylesheet.highlight.XStyleSheetColors;
import consulo.xstylesheet.psi.*;
import consulo.xstylesheet.psi.reference.BuildInSymbolElement;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class XStyleSheetHighlightVisitor implements HighlightVisitor, XStyleSheetColors
{
	private HighlightInfoHolder myHighlightInfoHolder;

	@Override
	public void visit(@Nonnull PsiElement psiElement)
	{
		psiElement.accept(new PsiElementVisitor()
		{
			@Override
			@RequiredReadAction
			public void visitElement(PsiElement element)
			{
				if(element instanceof PsiXStyleSheetVariableReference variableReference)
				{
					PsiElement resolved = variableReference.resolve();
					if(resolved != null)
					{
						highlightElement(resolved, variableReference.getElement());
					}
				}
				else
				{
					highlightElement(element, null);
				}

				element.acceptChildren(this);
			}
		});
	}

	@RequiredReadAction
	private void highlightElement(@Nonnull PsiElement element, @Nullable PsiElement targetElement)
	{
		if(element instanceof PsiXStyleSheetProperty property)
		{
			highlightName(targetElement == null ? property.getNameIdentifier() : targetElement, PROPERTY_NAME);
		}
		else if(element instanceof PsiXStyleSheetPropertyValuePart)
		{
			XStyleSheetPropertyValuePart valuePart = ((PsiXStyleSheetPropertyValuePart) element).getValuePart();
			if(valuePart != null)
			{
				myHighlightInfoHolder.addAll(valuePart.createHighlights((PsiXStyleSheetPropertyValuePart) element));
			}
		}
		else if(element instanceof PsiXStyleSheetSelectorAttribute attribute)
		{
			highlightName(targetElement == null ? attribute.getNameIdentifier() : targetElement, ATTRIBUTE_NAME);
		}
		else if(element instanceof XStyleSheetSimpleSelector)
		{
			PsiElement target = ((XStyleSheetSimpleSelector) element).getElement();
			if(target != null)
			{
				TextAttributesKey key;
				XStyleSheetSimpleSelectorType type = ((XStyleSheetSimpleSelector) element).getType();
				if(type == XStyleSheetSimpleSelectorType.CLASS)
				{
					key = XStyleSheetColors.SELECTOR_CLASS_NAME;
				}
				else if(type == XStyleSheetSimpleSelectorType.ID)
				{
					key = XStyleSheetColors.SELECTOR_ID_NAME;
				}
				else
				{
					key = XStyleSheetColors.SELECTOR_ELEMENT_NAME;
				}

				HighlightInfo.Builder builder = HighlightInfo.newHighlightInfo(HighlightInfoType.INFORMATION);
				builder.textAttributes(key);
				builder.range(target);
				myHighlightInfoHolder.add(builder.create());
			}
		}
		else if(element instanceof PsiXStyleSheetSelectorPseudoClass pseudoClass)
		{
			highlightName(targetElement == null ? pseudoClass.getNameIdentifier() : targetElement, PSEUDO_NAME);
		}
		else if(element instanceof PsiXStyleSheetFunctionCall)
		{
			PsiElement callElement = ((PsiXStyleSheetFunctionCall) element).getCallElement();

			PsiReference[] references = element.getReferences();
			for(PsiReference reference : references)
			{
				PsiElement resolvedElement = reference.resolve();
				if(resolvedElement instanceof BuildInSymbolElement)
				{
					HighlightInfo.Builder builder = HighlightInfo.newHighlightInfo(HighlightInfoType.INFORMATION);
					builder.textAttributes(XStyleSheetColors.KEYWORD);
					builder.range(callElement);

					myHighlightInfoHolder.add(builder.create());
				}
			}
		}

	}

	@RequiredReadAction
	private void highlightName(@Nullable PsiElement target, TextAttributesKey key)
	{
		if(target == null)
		{
			return;
		}

		HighlightInfo.Builder builder = HighlightInfo.newHighlightInfo(HighlightInfoType.INFORMATION);
		builder.textAttributes(key);
		builder.range(target);

		myHighlightInfoHolder.add(builder.create());
	}

	@Override
	public boolean analyze(@Nonnull PsiFile psiFile, boolean b, @Nonnull HighlightInfoHolder highlightInfoHolder, @Nonnull Runnable runnable)
	{
		myHighlightInfoHolder = highlightInfoHolder;
		runnable.run();
		return true;
	}
}
