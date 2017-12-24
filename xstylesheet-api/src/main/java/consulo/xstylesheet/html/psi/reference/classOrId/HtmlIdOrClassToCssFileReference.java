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

package consulo.xstylesheet.html.psi.reference.classOrId;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiPolyVariantReferenceBase;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ArrayUtil;
import consulo.annotations.RequiredReadAction;
import consulo.ide.IconDescriptorUpdaters;
import consulo.xstylesheet.html.psi.reference.file.HtmlHrefToCssFileReferenceProvider;
import consulo.xstylesheet.psi.PsiXStyleSheetRule;
import consulo.xstylesheet.psi.XStyleSheetFile;
import consulo.xstylesheet.psi.XStyleSheetSelector;
import consulo.xstylesheet.psi.XStyleSheetSimpleSelector;
import consulo.xstylesheet.psi.XStyleSheetSimpleSelectorType;
import consulo.xstylesheet.psi.reference.nameResolving.XStyleRuleCondition;
import consulo.xstylesheet.psi.reference.nameResolving.XStyleSheetRuleTypeCondition;

/**
 * @author VISTALL
 * @since 07.07.13.
 */
public class HtmlIdOrClassToCssFileReference extends PsiPolyVariantReferenceBase<PsiElement>
{
	private final XStyleSheetSimpleSelectorType myConditionType;

	public HtmlIdOrClassToCssFileReference(@NotNull PsiElement element, XStyleSheetSimpleSelectorType conditionType)
	{
		super(element);
		myConditionType = conditionType;
	}

	@RequiredReadAction
	@NotNull
	@Override
	public ResolveResult[] multiResolve(boolean b)
	{
		List<PsiXStyleSheetRule> rules = resolveRules(new XStyleSheetRuleTypeCondition(myConditionType, getElement().getText()));

		List<ResolveResult> resolveResults = new ArrayList<>(rules.size());
		for(PsiXStyleSheetRule rule : rules)
		{
			resolveResults.add(new PsiElementResolveResult(rule));
		}

		return resolveResults.isEmpty() ? ResolveResult.EMPTY_ARRAY : resolveResults.toArray(new ResolveResult[resolveResults.size()]);
	}

	@NotNull
	@Override
	@RequiredReadAction
	public Object[] getVariants()
	{
		List<PsiXStyleSheetRule> cssRules = resolveRules(new XStyleSheetRuleTypeCondition(myConditionType, null));

		List<LookupElementBuilder> items = new ArrayList<>(cssRules.size());
		for(PsiXStyleSheetRule cssRule : cssRules)
		{
			for(XStyleSheetSelector cssSelectorDeclaration : cssRule.getSelectors())
			{
				XStyleSheetSimpleSelector[] simpleSelectors = cssSelectorDeclaration.getSimpleSelectors();
				if(simpleSelectors.length == 0)
				{
					continue;
				}

				XStyleSheetSimpleSelector first = simpleSelectors[0];
				if(first.getType() == XStyleSheetSimpleSelectorType.ANY)
				{
					continue;
				}

				LookupElementBuilder item = LookupElementBuilder.create(first.getName());
				item = item.withIcon(IconDescriptorUpdaters.getIcon(cssSelectorDeclaration, 0));
				item = item.withTypeText(cssRule.getContainingFile().getName(), true);
				items.add(item);
			}
		}
		return ArrayUtil.toObjectArray(items);
	}

	@RequiredReadAction
	private List<PsiXStyleSheetRule> resolveRules(XStyleRuleCondition condition)
	{
		List<PsiXStyleSheetRule> resolveResults = new ArrayList<>();
		PsiElement[] psiElements = PsiTreeUtil.collectElements(getElement().getContainingFile(), new HtmlHrefToCssFileReferenceProvider.CssFileHrefFilter());
		for(PsiElement temp : psiElements)
		{
			PsiReference[] references = temp.getReferences();
			for(PsiReference reference : references)
			{
				PsiElement resolve = reference.resolve();
				if(resolve instanceof XStyleSheetFile)
				{
					List<PsiXStyleSheetRule> rules = ((XStyleSheetFile) resolve).getRoot().findRules(condition);
					for(PsiXStyleSheetRule rule : rules)
					{
						resolveResults.add(rule);
					}
				}
			}
		}
		return resolveResults;
	}
}