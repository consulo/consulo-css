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

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.Function;
import com.intellij.util.ProcessingContext;
import consulo.codeInsight.completion.CompletionProvider;
import consulo.css.lang.psi.CssFile;
import consulo.xstylesheet.definition.XStyleSheetProperty;
import consulo.xstylesheet.definition.XStyleSheetPropertyValueEntry;
import consulo.xstylesheet.definition.XStyleSheetPropertyValuePart;
import consulo.xstylesheet.definition.XStyleSheetTable;
import consulo.xstylesheet.psi.PsiXStyleSheetProperty;
import consulo.xstylesheet.psi.PsiXStyleSheetPropertyValuePart;
import consulo.xstylesheet.psi.PsiXStyleSheetRule;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class CssCompletionContributor extends CompletionContributor
{
	public CssCompletionContributor()
	{
		extend(CompletionType.BASIC, PlatformPatterns.psiElement().withParent(PsiXStyleSheetProperty.class), new CompletionProvider()
		{
			@Override
			public void addCompletions(@NotNull CompletionParameters completionParameters, ProcessingContext processingContext, @NotNull CompletionResultSet completionResultSet)
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

				XStyleSheetTable xStyleSheetTable = CssFile.getXStyleSheetTable(containingFile);
				for(XStyleSheetProperty property : xStyleSheetTable.getProperties())
				{
					if(alreadyExists.contains(property.getName()))
					{
						continue;
					}


					String defaultText = StringUtil.join(property.getInitialEntries(), new Function<XStyleSheetPropertyValueEntry, String>()
					{
						@Override
						public String fun(XStyleSheetPropertyValueEntry entry)
						{
							XStyleSheetPropertyValuePart[] parts = entry.getParts();

							if(parts.length > 0)
							{
								return parts[0].getValue();
							}
							return "";
						}
					}, ", ");

					StringBuilder b = new StringBuilder(property.getName());
					if(!defaultText.isEmpty())
					{
						b.append(": ");
						b.append(defaultText);
						b.append(";");
					}

					LookupElementBuilder builder = LookupElementBuilder.create(b.toString());
					builder = builder.withPresentableText(property.getName());
					if(!defaultText.isEmpty())
					{
						builder = builder.withTypeText(defaultText, true);
					}
					completionResultSet.addElement(builder);
				}
			}
		});

		extend(CompletionType.BASIC, PlatformPatterns.psiElement().withParent(PsiXStyleSheetPropertyValuePart.class), new CompletionProvider()
		{
			@Override
			public void addCompletions(@NotNull CompletionParameters completionParameters, ProcessingContext processingContext, @NotNull CompletionResultSet completionResultSet)
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
			}
		});
	}
}
