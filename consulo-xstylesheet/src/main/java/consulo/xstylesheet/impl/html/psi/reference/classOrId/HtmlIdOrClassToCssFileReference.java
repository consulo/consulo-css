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

package consulo.xstylesheet.impl.html.psi.reference.classOrId;

import consulo.annotation.access.RequiredReadAction;
import consulo.document.util.TextRange;
import consulo.language.editor.completion.lookup.LookupElementBuilder;
import consulo.language.icon.IconDescriptorUpdaters;
import consulo.language.psi.*;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.util.collection.ArrayUtil;
import consulo.xstylesheet.impl.html.psi.reference.file.HtmlHrefToCssFileReferenceProvider;
import consulo.platform.base.icon.PlatformIconGroup;
import consulo.xstylesheet.psi.*;
import consulo.xstylesheet.psi.index.XStyleSheetClassNameIndex;
import consulo.xstylesheet.psi.index.XStyleSheetDeclarations;
import consulo.xstylesheet.psi.reference.nameResolving.XStyleRuleCondition;
import consulo.xstylesheet.psi.reference.nameResolving.XStyleSheetRuleTypeCondition;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author VISTALL
 * @since 2013-07-07
 */
public class HtmlIdOrClassToCssFileReference extends PsiPolyVariantReferenceBase<PsiElement> {
    private final XStyleSheetSimpleSelectorType myConditionType;

    /**
     * @param rangeInElement the id, or one class of a class list. The reference is soft: a class may come from where no
     *                       stylesheet of the project declares it, for example from a CSS framework.
     */
    public HtmlIdOrClassToCssFileReference(PsiElement element, TextRange rangeInElement, XStyleSheetSimpleSelectorType conditionType) {
        super(element, rangeInElement, true);
        myConditionType = conditionType;
    }

    /**
     * The rules of the stylesheets linked from the file; for a class without them, the class selectors of the stylesheets
     * of the project, as a page may get its stylesheets from a layout or a bundler (stylesheets are seldom in source roots,
     * so the whole project is searched)
     */
    @RequiredReadAction
    @Override
    public ResolveResult[] multiResolve(boolean b) {
        List<PsiXStyleSheetRule> rules = resolveRules(new XStyleSheetRuleTypeCondition(myConditionType, getValue()));

        List<ResolveResult> resolveResults = new ArrayList<>(rules.size());
        for (PsiXStyleSheetRule rule : rules) {
            resolveResults.add(new PsiElementResolveResult(rule));
        }
        if (resolveResults.isEmpty() && myConditionType == XStyleSheetSimpleSelectorType.CLASS) {
            PsiElement element = getElement();
            List<XStyleSheetSimpleSelector> selectors =
                XStyleSheetClassNameIndex.findAllClassSelectors(element.getProject(), getValue(), GlobalSearchScope.projectScope(element.getProject()));
            for (XStyleSheetSimpleSelector selector : XStyleSheetDeclarations.preferProjectOwn(selectors)) {
                resolveResults.add(new PsiElementResolveResult(selector));
            }
        }

        return resolveResults.isEmpty() ? ResolveResult.EMPTY_ARRAY : resolveResults.toArray(new ResolveResult[resolveResults.size()]);
    }

    @Override
    @RequiredReadAction
    public Object[] getVariants() {
        List<PsiXStyleSheetRule> cssRules = resolveRules(new XStyleSheetRuleTypeCondition(myConditionType, null));

        List<LookupElementBuilder> items = new ArrayList<>(cssRules.size());
        for (PsiXStyleSheetRule cssRule : cssRules) {
            for (XStyleSheetSelector cssSelectorDeclaration : cssRule.getSelectors()) {
                XStyleSheetSimpleSelector[] simpleSelectors = cssSelectorDeclaration.getSimpleSelectors();
                if (simpleSelectors.length == 0) {
                    continue;
                }

                XStyleSheetSimpleSelector first = simpleSelectors[0];
                if (first.getType() == XStyleSheetSimpleSelectorType.ANY) {
                    continue;
                }

                LookupElementBuilder item = LookupElementBuilder.create(first.getName());
                item = item.withIcon(IconDescriptorUpdaters.getIcon(cssSelectorDeclaration, 0));
                item = item.withTypeText(cssRule.getContainingFile().getName(), true);
                items.add(item);
            }
        }
        if (myConditionType == XStyleSheetSimpleSelectorType.CLASS) {
            Set<String> linked = new HashSet<>();
            for (LookupElementBuilder item : items) {
                linked.add(item.getLookupString());
            }
            PsiElement element = getElement();
            for (String className : XStyleSheetClassNameIndex.getAllClassNamesOfAll(element.getProject(), GlobalSearchScope.projectScope(element.getProject()))) {
                if (!linked.contains(className)) {
                    items.add(LookupElementBuilder.create(className).withIcon(PlatformIconGroup.nodesClass()));
                }
            }
        }
        return ArrayUtil.toObjectArray(items);
    }

    @RequiredReadAction
    private List<PsiXStyleSheetRule> resolveRules(XStyleRuleCondition condition) {
        List<PsiXStyleSheetRule> resolveResults = new ArrayList<>();
        PsiElement[] psiElements =
            PsiTreeUtil.collectElements(getElement().getContainingFile(), new HtmlHrefToCssFileReferenceProvider.CssFileHrefFilter());
        for (PsiElement temp : psiElements) {
            PsiReference[] references = temp.getReferences();
            for (PsiReference reference : references) {
                PsiElement resolve = reference.resolve();
                if (resolve instanceof XStyleSheetFile styleSheetFile) {
                    List<PsiXStyleSheetRule> rules = styleSheetFile.getRoot().findRules(condition);
                    for (PsiXStyleSheetRule rule : rules) {
                        resolveResults.add(rule);
                    }
                }
            }
        }
        return resolveResults;
    }
}