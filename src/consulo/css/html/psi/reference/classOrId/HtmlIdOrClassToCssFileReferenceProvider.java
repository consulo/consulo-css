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

import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.psi.xml.XmlTokenType;
import com.intellij.util.ProcessingContext;
import consulo.css.lang.psi.reference.nameResolving.CssSimpleRuleConditionType;

/**
 * @author VISTALL
 * @since 07.07.13.
 */
public class HtmlIdOrClassToCssFileReferenceProvider extends PsiReferenceProvider
{
	private final CssSimpleRuleConditionType myCssRefTo;

	public HtmlIdOrClassToCssFileReferenceProvider(CssSimpleRuleConditionType cssRefTo)
	{
		myCssRefTo = cssRefTo;
	}

	@NotNull
	@Override
	public PsiReference[] getReferencesByElement(@NotNull PsiElement psiElement, @NotNull ProcessingContext processingContext)
	{
		ASTNode value = psiElement.getNode().findChildByType(XmlTokenType.XML_ATTRIBUTE_VALUE_TOKEN);
		if(value == null)
		{
			return PsiReference.EMPTY_ARRAY;
		}

		return new PsiReference[]{new HtmlIdOrClassToCssFileReference(value.getPsi(), myCssRefTo)};
	}
}