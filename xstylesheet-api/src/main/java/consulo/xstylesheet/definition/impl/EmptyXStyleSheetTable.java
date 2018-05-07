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

package consulo.xstylesheet.definition.impl;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import consulo.xstylesheet.definition.XStyleSheetProperty;
import consulo.xstylesheet.definition.XStyleSheetTable;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class EmptyXStyleSheetTable implements XStyleSheetTable
{
	public static final XStyleSheetTable INSTANCE = new EmptyXStyleSheetTable();

	@Nullable
	@Override
	public XStyleSheetProperty findProperty(@Nonnull String propertyName)
	{
		return null;
	}

	@Nonnull
	@Override
	public List<XStyleSheetProperty> getProperties()
	{
		return Collections.emptyList();
	}
}
