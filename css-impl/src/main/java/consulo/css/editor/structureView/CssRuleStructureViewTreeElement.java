/*
 * Copyright 2013-2026 consulo.io
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
package consulo.css.editor.structureView;

import consulo.annotation.access.RequiredReadAction;
import consulo.css.lang.psi.CssAtRule;
import consulo.css.lang.psi.CssMedia;
import consulo.css.lang.psi.CssMediaQueryList;
import consulo.fileEditor.structureView.StructureViewTreeElement;
import consulo.language.editor.structureView.PsiTreeElementBase;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiWhiteSpace;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.localize.LocalizeValue;
import consulo.navigation.ItemPresentation;
import consulo.navigation.NavigationItem;
import consulo.util.lang.StringUtil;
import consulo.xstylesheet.psi.PsiXStyleSheetBlock;
import consulo.xstylesheet.psi.PsiXStyleSheetRule;
import consulo.xstylesheet.psi.XStyleSheetSelectorList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A rule ({@code a, b { }}) or an at-rule ({@code @media all { }}) of the structure view, with the rules and at-rules of
 * its block as children.
 *
 * @since 2026-10-30
 */
public class CssRuleStructureViewTreeElement extends PsiTreeElementBase<PsiElement> {
    public CssRuleStructureViewTreeElement(PsiElement element) {
        super(element);
    }

    /**
     * @return whether the element is shown in the structure view: a rule or an at-rule
     */
    public static boolean isStructureElement(PsiElement element) {
        return element instanceof PsiXStyleSheetRule || element instanceof CssAtRule;
    }

    /**
     * The structure view elements of the rules and at-rules among the children of a stylesheet root or block.
     */
    @RequiredReadAction
    public static List<StructureViewTreeElement> collectChildren(PsiElement container) {
        List<StructureViewTreeElement> list = new ArrayList<>();
        for (PsiElement child : container.getChildren()) {
            if (addDialectElements(child, list)) {
                continue;
            }
            if (isStructureElement(child)) {
                list.add(new CssRuleStructureViewTreeElement(child));
            }
        }
        return list;
    }

    private static boolean addDialectElements(PsiElement element, List<StructureViewTreeElement> list) {
        for (CssStructureViewElementsProvider provider : CssStructureViewElementsProvider.EP_NAME.getExtensionList()) {
            if (provider.isMyContext(element)) {
                Collection<? extends StructureViewTreeElement> elements = provider.createStructureViewElements(element);
                if (!elements.isEmpty()) {
                    list.addAll(elements);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    @RequiredReadAction
    public Collection<StructureViewTreeElement> getChildrenBase() {
        PsiElement element = getElement();
        PsiXStyleSheetBlock block = element == null ? null : PsiTreeUtil.getChildOfType(element, PsiXStyleSheetBlock.class);
        return block == null ? List.of() : collectChildren(block);
    }

    @Override
    @RequiredReadAction
    public LocalizeValue getPresentableText() {
        PsiElement element = getElement();
        return element == null ? LocalizeValue.empty() : LocalizeValue.of(getPresentableText(element));
    }

    /**
     * The text of a rule is its selector list, an at-rule shows its name without '@' (and the queries of {@code @media}),
     * unless the element has its own presentation.
     */
    @RequiredReadAction
    private static String getPresentableText(PsiElement element) {
        ItemPresentation presentation = element instanceof NavigationItem item ? item.getPresentation() : null;
        if (presentation != null) {
            String text = presentation.getPresentableText().get();
            if (!StringUtil.isEmpty(text)) {
                return text;
            }
        }
        if (element instanceof PsiXStyleSheetRule rule) {
            XStyleSheetSelectorList selectorList = rule.getSelectorList();
            return selectorList == null ? "" : collapseWhiteSpaces(selectorList.getText());
        }
        String name = getAtRuleName(element);
        if (element instanceof CssMedia) {
            CssMediaQueryList queryList = PsiTreeUtil.getChildOfType(element, CssMediaQueryList.class);
            if (queryList != null) {
                return name + " " + collapseWhiteSpaces(queryList.getText());
            }
        }
        return name;
    }

    private static String getAtRuleName(PsiElement element) {
        PsiElement first = element.getFirstChild();
        while (first instanceof PsiWhiteSpace) {
            first = first.getNextSibling();
        }
        String text = first == null ? "" : first.getText();
        return StringUtil.trimStart(text, "@");
    }

    private static String collapseWhiteSpaces(String text) {
        return text.trim().replaceAll("\\s+", " ");
    }
}
