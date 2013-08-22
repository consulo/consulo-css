package org.consulo.css.html.psi.reference.classOrId;

import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.ide.IconDescriptorUpdaters;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ArrayUtil;
import org.consulo.css.html.psi.reference.file.HtmlHrefToCssFileReferenceProvider;
import org.consulo.css.lang.psi.CssFile;
import org.consulo.css.lang.psi.CssRule;
import org.consulo.css.lang.psi.CssSelectorDeclaration;
import org.consulo.css.lang.psi.CssSelectorReference;
import org.consulo.css.lang.psi.reference.nameResolving.CssSimpleRuleConditionType;
import org.consulo.css.lang.psi.reference.nameResolving.CssSimpleRuleOnlyTypeCondition;
import org.consulo.css.lang.psi.reference.nameResolving.CssSimpleRuleWithNameCondition;
import org.consulo.xstylesheet.psi.reference.nameResolving.XStyleRuleCondition;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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