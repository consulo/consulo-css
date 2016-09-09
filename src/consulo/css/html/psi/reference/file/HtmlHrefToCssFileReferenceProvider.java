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

package consulo.css.html.psi.reference.file;

import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.Condition;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFileSystemItem;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.psi.filters.ElementFilter;
import com.intellij.psi.filters.ElementFilterBase;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReferenceSet;
import com.intellij.psi.util.PsiElementFilter;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlTokenType;
import com.intellij.util.ProcessingContext;
import consulo.css.lang.psi.CssFile;


/**
 * @author VISTALL
 * @since 07.07.13.
 */
public class HtmlHrefToCssFileReferenceProvider extends PsiReferenceProvider
{
	public static class CssFileHrefFilter extends ElementFilterBase<XmlAttributeValue> implements PsiElementFilter
	{
		public CssFileHrefFilter()
		{
			super(XmlAttributeValue.class);
		}

		@Override
		protected boolean isElementAcceptable(XmlAttributeValue xmlAttributeValue, PsiElement psiElement)
		{
			XmlAttribute xmlAttribute = (XmlAttribute) xmlAttributeValue.getParent();
			if(!"href".equalsIgnoreCase(xmlAttribute.getName()))
			{
				return false;
			}
			XmlTag xmlTag = xmlAttribute.getParent();//XmlAttribute -> XmlTag

			if("link".equalsIgnoreCase(xmlTag.getName()))
			{
				String rel = xmlTag.getAttributeValue("rel");
				if(!"stylesheet".equalsIgnoreCase(rel))
				{
					return false;
				}

				String type = xmlTag.getAttributeValue("type");
				if(!"text/css".equalsIgnoreCase(type))
				{
					return false;
				}
			}
			return true;
		}

		@Override
		public boolean isAccepted(PsiElement psiElement)
		{
			return psiElement instanceof XmlAttributeValue && isElementAcceptable((XmlAttributeValue) psiElement, psiElement);
		}
	}

	@NotNull
	@Override
	public PsiReference[] getReferencesByElement(@NotNull PsiElement psiElement, @NotNull ProcessingContext processingContext)
	{
		XmlAttributeValue xmlAttributeValue = (XmlAttributeValue) psiElement;
		ASTNode value = xmlAttributeValue.getNode().findChildByType(XmlTokenType.XML_ATTRIBUTE_VALUE_TOKEN);
		if(value == null)
		{
			return PsiReference.EMPTY_ARRAY;
		}
		FileReferenceSet fileReferenceSet = new FileReferenceSet(value.getPsi())
		{
			@Override
			protected Condition<PsiFileSystemItem> getReferenceCompletionFilter()
			{
				return new Condition<PsiFileSystemItem>()
				{
					@Override
					public boolean value(PsiFileSystemItem psiFileSystemItem)
					{
						return psiFileSystemItem instanceof CssFile || psiFileSystemItem instanceof PsiDirectory;
					}
				};
			}
		};
		fileReferenceSet.setEmptyPathAllowed(false);

		return fileReferenceSet.getAllReferences();
	}

	public ElementFilter getElementFilter()
	{
		return new CssFileHrefFilter();
	}
}
