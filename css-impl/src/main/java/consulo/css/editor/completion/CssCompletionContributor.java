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

package consulo.css.editor.completion;

import consulo.annotation.component.ExtensionImpl;
import consulo.css.lang.CssLanguage;
import consulo.language.Language;
import consulo.language.editor.completion.CompletionContributor;
import consulo.language.editor.completion.CompletionType;
import consulo.language.editor.completion.lookup.LookupElementBuilder;
import consulo.language.pattern.PlatformPatterns;
import consulo.language.psi.PsiFile;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.util.lang.StringUtil;
import consulo.xstylesheet.definition.XStyleSheetProperty;
import consulo.xstylesheet.definition.XStyleSheetPropertyValuePart;
import consulo.xstylesheet.definition.XStyleSheetTable;
import consulo.xstylesheet.icon.XStyleSheetIconGroup;
import consulo.xstylesheet.psi.PsiXStyleSheetProperty;
import consulo.xstylesheet.psi.PsiXStyleSheetPropertyValuePart;
import consulo.xstylesheet.psi.PsiXStyleSheetRule;
import consulo.xstylesheet.psi.XStyleSheetFile;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
@ExtensionImpl
public class CssCompletionContributor extends CompletionContributor
{
	public CssCompletionContributor()
	{
		extend(CompletionType.BASIC, PlatformPatterns.psiElement().withParent(PsiXStyleSheetProperty.class), (completionParameters, processingContext, completionResultSet) ->
		{
			PsiXStyleSheetRule rule = PsiTreeUtil.getParentOfType(completionParameters.getPosition(), PsiXStyleSheetRule.class);
			if(rule == null)
			{
				return;
			}

			PsiFile containingFile = completionParameters.getOriginalFile();

			List<String> alreadyExists = new ArrayList<>();
			for(PsiXStyleSheetProperty property : rule.getProperties())
			{
				alreadyExists.add(property.getName());
			}

			XStyleSheetTable xStyleSheetTable = XStyleSheetFile.getXStyleSheetTable(containingFile);
			for(XStyleSheetProperty property : xStyleSheetTable.getProperties())
			{
				if(alreadyExists.contains(property.getName()))
				{
					continue;
				}

				String defaultText = StringUtil.join(property.getInitialEntries(), entry ->
				{
					XStyleSheetPropertyValuePart[] parts = entry.getParts();

					if(parts.length > 0)
					{
						return parts[0].getValue();
					}
					return "";
				}, ", ");

				StringBuilder b = new StringBuilder(property.getName()).append(": ");
				if(!defaultText.isEmpty())
				{
					b.append(defaultText);
					b.append(";");
				}

				LookupElementBuilder builder = LookupElementBuilder.create(b.toString());
				builder = builder.withPresentableText(property.getName());
				builder = builder.withIcon(XStyleSheetIconGroup.property());
				if(!defaultText.isEmpty())
				{
					builder = builder.withTypeText(defaultText, true);
				}
				completionResultSet.addElement(builder);
			}
		});

		extend(CompletionType.BASIC, PlatformPatterns.psiElement().withParent(PsiXStyleSheetPropertyValuePart.class), (completionParameters, processingContext, completionResultSet) ->
		{
			PsiXStyleSheetPropertyValuePart parent = (PsiXStyleSheetPropertyValuePart) completionParameters.getPosition().getParent();
			if(parent == null)
			{
				return;
			}

			for(XStyleSheetPropertyValuePart o : parent.getValueParts())
			{
				completionResultSet.addAllElements(o.getLookupElements());
			}
		});

		extend(CompletionType.BASIC, PlatformPatterns.psiElement().withParent(PsiXStyleSheetPropertyValuePart.class), (completionParameters, processingContext, completionResultSet) ->
		{
			completionResultSet.addElement(LookupElementBuilder.create("!important").withLookupString("important").withPresentableText("!important").bold());
		});
	}

	@Nonnull
	@Override
	public Language getLanguage()
	{
		return CssLanguage.INSTANCE;
	}
}
