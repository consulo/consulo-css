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

package consulo.xstylesheet.html;

import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceRegistrar;
import com.intellij.xml.util.XmlUtil;
import consulo.xstylesheet.html.psi.reference.classOrId.HtmlIdOrClassToCssFileReferenceProvider;
import consulo.xstylesheet.html.psi.reference.file.HtmlHrefToCssFileReferenceProvider;
import consulo.xstylesheet.psi.XStyleSheetSimpleSelectorType;

/**
 * @author VISTALL
 * @since 07.07.13.
 */
public class XStyleSheetToHtmlReferenceContributor extends PsiReferenceContributor
{
	@Override
	public void registerReferenceProviders(PsiReferenceRegistrar psiReferenceRegistrar)
	{
		XmlUtil.registerXmlAttributeValueReferenceProvider(psiReferenceRegistrar, new String[]{"id"}, null, false, new HtmlIdOrClassToCssFileReferenceProvider(XStyleSheetSimpleSelectorType.ID));

		XmlUtil.registerXmlAttributeValueReferenceProvider(psiReferenceRegistrar, new String[]{"class"}, null, false, new HtmlIdOrClassToCssFileReferenceProvider(XStyleSheetSimpleSelectorType.CLASS));

		HtmlHrefToCssFileReferenceProvider provider = new HtmlHrefToCssFileReferenceProvider();

		XmlUtil.registerXmlAttributeValueReferenceProvider(psiReferenceRegistrar, new String[]{"href"}, provider.getElementFilter(), false, provider);
	}
}
