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

import consulo.document.util.TextRange;
import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiReference;
import consulo.language.psi.PsiReferenceProvider;
import consulo.language.util.ProcessingContext;
import consulo.xml.language.psi.XmlTokenType;
import consulo.xstylesheet.psi.XStyleSheetSimpleSelectorType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author VISTALL
 * @since 2013-07-07
 */
public class HtmlIdOrClassToCssFileReferenceProvider extends PsiReferenceProvider {
    private final XStyleSheetSimpleSelectorType myCssRefTo;

    public HtmlIdOrClassToCssFileReferenceProvider(XStyleSheetSimpleSelectorType cssRefTo) {
        myCssRefTo = cssRefTo;
    }

    @Override
    public PsiReference[] getReferencesByElement(PsiElement psiElement, ProcessingContext processingContext) {
        ASTNode value = psiElement.getNode().findChildByType(XmlTokenType.XML_ATTRIBUTE_VALUE_TOKEN);
        if (value == null) {
            return PsiReference.EMPTY_ARRAY;
        }

        String text = value.getText();
        if (myCssRefTo != XStyleSheetSimpleSelectorType.CLASS) {
            return new PsiReference[]{new HtmlIdOrClassToCssFileReference(value.getPsi(), new TextRange(0, text.length()), myCssRefTo)};
        }

        // a reference for each class of the space separated list
        List<PsiReference> references = new ArrayList<>();
        int start = -1;
        for (int i = 0; i <= text.length(); i++) {
            boolean separator = i == text.length() || Character.isWhitespace(text.charAt(i));
            if (separator && start >= 0) {
                references.add(new HtmlIdOrClassToCssFileReference(value.getPsi(), new TextRange(start, i), myCssRefTo));
                start = -1;
            }
            else if (!separator && start < 0) {
                start = i;
            }
        }
        return references.toArray(PsiReference.EMPTY_ARRAY);
    }
}
