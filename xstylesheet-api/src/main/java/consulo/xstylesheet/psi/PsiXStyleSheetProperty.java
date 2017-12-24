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

package consulo.xstylesheet.psi;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import consulo.annotations.RequiredReadAction;
import consulo.xstylesheet.definition.XStyleSheetProperty;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public interface PsiXStyleSheetProperty extends PsiElement, PsiNameIdentifierOwner
{
	@Nullable
	XStyleSheetProperty getXStyleSheetProperty();

	@NotNull
	PsiXStyleSheetPropertyValuePart[] getParts();

	@Nullable
	String getName();

	@RequiredReadAction
	boolean isImportant();
}