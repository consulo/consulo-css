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

package consulo.css.lang.psi;

import java.util.List;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElementVisitor;
import consulo.annotations.RequiredReadAction;
import consulo.css.lang.CssLanguage;
import consulo.xstylesheet.psi.PsiXStyleSheetRule;
import consulo.xstylesheet.psi.XStyleSheetFile;
import consulo.xstylesheet.psi.XStyleSheetRoot;
import consulo.xstylesheet.psi.reference.nameResolving.XStyleRuleCondition;

/**
 * @author VISTALL
 * @since 02.07.13.
 */
public class CssFile extends PsiFileBase implements XStyleSheetFile
{
	public CssFile(@NotNull FileViewProvider provider)
	{
		super(provider, CssLanguage.INSTANCE);
	}

	@Override
	public void accept(@NotNull PsiElementVisitor psiElementVisitor)
	{
		psiElementVisitor.visitFile(this);
	}

	@Nullable
	@RequiredReadAction
	public PsiXStyleSheetRule findRule(@NotNull XStyleRuleCondition condition)
	{
		return getRoot().findRule(condition);
	}

	@NotNull
	@RequiredReadAction
	public List<PsiXStyleSheetRule> findRules(@NotNull XStyleRuleCondition condition)
	{
		return getRoot().findRules(condition);
	}

	@NotNull
	@Override
	public XStyleSheetRoot getRoot()
	{
		return Objects.requireNonNull(findChildByClass(XStyleSheetRoot.class));
	}
}
