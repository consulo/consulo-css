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

package consulo.css.html.psi.reference.classOrId;

import java.util.ArrayList;
import java.util.List;

import consulo.css.html.psi.reference.file.HtmlHrefToCssFileReferenceProvider;
import consulo.css.lang.psi.CssFile;
import consulo.css.lang.psi.CssRule;
import consulo.css.lang.psi.CssSelectorDeclaration;
import consulo.css.lang.psi.CssSelectorReference;
import consulo.css.lang.psi.reference.nameResolving.CssSimpleRuleConditionType;
import consulo.css.lang.psi.reference.nameResolving.CssSimpleRuleOnlyTypeCondition;
import consulo.css.lang.psi.reference.nameResolving.CssSimpleRuleWithNameCondition;
import consulo.xstylesheet.psi.reference.nameResolving.XStyleRuleCondition;
import org.jetbrains.annotations.NotNull;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiPolyVariantReferenceBase;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ArrayUtil;
import consulo.ide.IconDescriptorUpdaters;

/**
 * @author VISTALL
 * @since 07.07.13.
 */
public class HtmlIdOrClassToCssFileReference extends PsiPolyVariantReferenceBase<PsiElement> {
	private CssSimpleRuleWithNameCondition myCondition;

	public HtmlIdOrClassToCssFileReference(@NotNull PsiElement element, CssSimpleRuleConditionType cssRefTo) {
		super(element);
		myCondition = new CssSimpleRuleWithNameCondition(cssRefTo, element.getText().trim());
	}

	@NotNull
	@Override
	public ResolveResult[] multiResolve(boolean b) {
		List<CssRule> rules = resolveRules(myCondition);

		List<ResolveResult> resolveResults = new ArrayList<ResolveResult>(rules.size());
		for (CssRule rule : rules) {
			resolveResults.add(new PsiElementResolveResult(rule));
		}

		return resolveResults.isEmpty() ? ResolveResult.EMPTY_ARRAY : resolveResults.toArray(new ResolveResult[resolveResults.size()]);
	}

	@NotNull
	@Override
	public Object[] getVariants() {
		List<CssRule> cssRules = resolveRules(new CssSimpleRuleOnlyTypeCondition(myCondition.getConditionType()));

		List<LookupElementBuilder> items = new ArrayList<LookupElementBuilder>(cssRules.size());
		for (CssRule cssRule : cssRules) {
			for (CssSelectorDeclaration cssSelectorDeclaration : cssRule.getSelectorDeclarations()) {
				CssSelectorReference[] selectorReferences = cssSelectorDeclaration.getSelectorReferences();
				if(selectorReferences.length != 1) {
					continue;
				}

				LookupElementBuilder item = LookupElementBuilder.create(selectorReferences[0].getName());
				item = item.withIcon(IconDescriptorUpdaters.getIcon(cssSelectorDeclaration, 0));
				item = item.withTypeText(cssRule.getContainingFile().getName(), true);
				items.add(item);
			}
		}
		return ArrayUtil.toObjectArray(items);
	}

	private List<CssRule> resolveRules(XStyleRuleCondition condition) {
		List<CssRule> resolveResults = new ArrayList<CssRule>();
		PsiElement[] psiElements = PsiTreeUtil.collectElements(getElement().getContainingFile(), new HtmlHrefToCssFileReferenceProvider.CssFileHrefFilter());
		for (PsiElement temp : psiElements) {
			PsiReference[] references = temp.getReferences();
			for (PsiReference reference : references) {
				PsiElement resolve = reference.resolve();
				if (resolve instanceof CssFile) {
					List<CssRule> rules = ((CssFile) resolve).findRules(condition);
					for (CssRule rule : rules) {
						resolveResults.add(rule);
					}
				}
			}
		}
		return resolveResults;
	}
}