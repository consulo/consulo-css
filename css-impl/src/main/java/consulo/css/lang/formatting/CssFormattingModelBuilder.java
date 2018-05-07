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

package consulo.css.lang.formatting;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.intellij.formatting.Block;
import com.intellij.formatting.FormattingModel;
import com.intellij.formatting.FormattingModelBuilder;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.formatter.FormattingDocumentModelImpl;
import com.intellij.psi.formatter.PsiBasedFormattingModel;

/**
 * @author VISTALL
 * @since 08.07.13.
 */
public class CssFormattingModelBuilder implements FormattingModelBuilder
{
	@Nonnull
	@Override
	public FormattingModel createModel(PsiElement element, CodeStyleSettings codeStyleSettings)
	{
		final PsiFile file = element.getContainingFile();
		FormattingDocumentModelImpl model = FormattingDocumentModelImpl.createOn(element.getContainingFile());
		Block rootBlock = new CssFormattingBlock(element.getNode(), null, null);
		return new PsiBasedFormattingModel(file, rootBlock, model);
	}

	@Nullable
	@Override
	public TextRange getRangeAffectingIndent(PsiFile psiFile, int i, ASTNode astNode)
	{
		return null;
	}
}
