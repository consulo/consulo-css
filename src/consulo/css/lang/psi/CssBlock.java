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

import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;
import consulo.xstylesheet.psi.PsiXStyleSheetBlock;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class CssBlock extends CssElement implements PsiXStyleSheetBlock
{
	public CssBlock(@NotNull ASTNode node)
	{
		super(node);
	}

	@Override
	public CssProperty[] getProperties()
	{
		return findChildrenByClass(CssProperty.class);
	}
}
