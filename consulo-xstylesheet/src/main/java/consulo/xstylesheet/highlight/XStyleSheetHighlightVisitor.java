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

package consulo.xstylesheet.highlight;

import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.codeInsight.daemon.impl.HighlightInfoType;
import com.intellij.codeInsight.daemon.impl.HighlightVisitor;
import com.intellij.codeInsight.daemon.impl.analysis.HighlightInfoHolder;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.*;
import consulo.annotation.access.RequiredReadAction;
import consulo.xstylesheet.definition.XStyleSheetPropertyValuePart;
import consulo.xstylesheet.psi.*;
import consulo.xstylesheet.psi.reference.impl.BuildInSymbolElement;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class XStyleSheetHighlightVisitor implements HighlightVisitor, XStyleSheetColors
{
	private HighlightInfoHolder myHighlightInfoHolder;

	@Override
	public boolean suitableForFile(@Nonnull PsiFile psiFile)
	{
		return true;
	}

	@Override
	public void visit(@Nonnull PsiElement psiElement)
	{
		psiElement.accept(new PsiElementVisitor()
		{
			@Override
			@RequiredReadAction
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
				else if(element instanceof PsiXStyleSheetSelectorPseudoClass)
				{
					highlightName((PsiNameIdentifierOwner) element, PSEUDO_NAME);
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

				element.acceptChildren(this);
			}
		});
	}

	@RequiredReadAction
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
	public boolean analyze(@Nonnull PsiFile psiFile, boolean b, @Nonnull HighlightInfoHolder highlightInfoHolder, @Nonnull Runnable runnable)
	{
		myHighlightInfoHolder = highlightInfoHolder;
		runnable.run();
		return true;
	}

	@Nonnull
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
