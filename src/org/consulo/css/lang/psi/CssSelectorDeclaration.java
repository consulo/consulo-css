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

package org.consulo.css.lang.psi;

import com.intellij.lang.ASTNode;
import org.consulo.xstylesheet.psi.PsiXStyleSheetSelectorDeclaration;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 11.07.13.
 */
public class CssSelectorDeclaration extends CssElement implements PsiXStyleSheetSelectorDeclaration {
	public static final CssSelectorDeclaration[] EMPTY_ARRAY = new CssSelectorDeclaration[0];

	public CssSelectorDeclaration(@NotNull ASTNode node) {
		super(node);
	}

	@Override
	public CssSelectorReference[] getSelectorReferences() {
		return findChildrenByClass(CssSelectorReference.class);
	}
}
