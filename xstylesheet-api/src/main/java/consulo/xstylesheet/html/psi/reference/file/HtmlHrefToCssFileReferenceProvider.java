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

package consulo.xstylesheet.html.psi.reference.file;

import consulo.language.ast.ASTNode;
import consulo.language.psi.*;
import consulo.language.psi.filter.ElementFilter;
import consulo.language.psi.filter.ElementFilterBase;
import consulo.language.psi.path.FileReferenceSet;
import consulo.language.psi.util.PsiElementFilter;
import consulo.language.util.ProcessingContext;
import consulo.util.lang.function.Condition;
import consulo.xml.psi.xml.XmlAttribute;
import consulo.xml.psi.xml.XmlAttributeValue;
import consulo.xml.psi.xml.XmlTag;
import consulo.xml.psi.xml.XmlTokenType;
import consulo.xstylesheet.psi.XStyleSheetFile;

import javax.annotation.Nonnull;


/**
 * @author VISTALL
 * @since 2013-07-07
 */
public class HtmlHrefToCssFileReferenceProvider extends PsiReferenceProvider {
    public static class CssFileHrefFilter extends ElementFilterBase<XmlAttributeValue> implements PsiElementFilter {
        public CssFileHrefFilter() {
            super(XmlAttributeValue.class);
        }

        @Override
        protected boolean isElementAcceptable(XmlAttributeValue xmlAttributeValue, PsiElement psiElement) {
            XmlAttribute xmlAttribute = (XmlAttribute)xmlAttributeValue.getParent();
            if (!"href".equalsIgnoreCase(xmlAttribute.getName())) {
                return false;
            }
            XmlTag xmlTag = xmlAttribute.getParent();//XmlAttribute -> XmlTag

            if ("link".equalsIgnoreCase(xmlTag.getName())) {
                String rel = xmlTag.getAttributeValue("rel");
                if ("stylesheet".equalsIgnoreCase(rel)) {
                    return true;
                }
            }
            return true;
        }

        @Override
        public boolean isAccepted(PsiElement psiElement) {
            return psiElement instanceof XmlAttributeValue xmlAttributeValue && isElementAcceptable(xmlAttributeValue, psiElement);
        }
    }

    @Nonnull
    @Override
    public PsiReference[] getReferencesByElement(@Nonnull PsiElement psiElement, @Nonnull ProcessingContext processingContext) {
        XmlAttributeValue xmlAttributeValue = (XmlAttributeValue)psiElement;
        ASTNode value = xmlAttributeValue.getNode().findChildByType(XmlTokenType.XML_ATTRIBUTE_VALUE_TOKEN);
        if (value == null) {
            return PsiReference.EMPTY_ARRAY;
        }
        FileReferenceSet fileReferenceSet = new FileReferenceSet(value.getPsi()) {
            @Override
            public Condition<PsiFileSystemItem> getReferenceCompletionFilter() {
                return item -> item instanceof XStyleSheetFile || item instanceof PsiDirectory;
            }
        };
        fileReferenceSet.setEmptyPathAllowed(false);

        return fileReferenceSet.getAllReferences();
    }

    public ElementFilter getElementFilter() {
        return new CssFileHrefFilter();
    }
}
