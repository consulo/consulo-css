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

import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiReference;
import consulo.language.psi.PsiReferenceProvider;
import consulo.language.util.ProcessingContext;
import consulo.xml.psi.xml.XmlTokenType;
import consulo.xstylesheet.psi.XStyleSheetSimpleSelectorType;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 2013-07-07
 */
public class HtmlIdOrClassToCssFileReferenceProvider extends PsiReferenceProvider {
    private final XStyleSheetSimpleSelectorType myCssRefTo;

    public HtmlIdOrClassToCssFileReferenceProvider(XStyleSheetSimpleSelectorType cssRefTo) {
        myCssRefTo = cssRefTo;
    }

    @Nonnull
    @Override
    public PsiReference[] getReferencesByElement(@Nonnull PsiElement psiElement, @Nonnull ProcessingContext processingContext) {
        ASTNode value = psiElement.getNode().findChildByType(XmlTokenType.XML_ATTRIBUTE_VALUE_TOKEN);
        if (value == null) {
            return PsiReference.EMPTY_ARRAY;
        }

        return new PsiReference[]{new HtmlIdOrClassToCssFileReference(value.getPsi(), myCssRefTo)};
    }
}
