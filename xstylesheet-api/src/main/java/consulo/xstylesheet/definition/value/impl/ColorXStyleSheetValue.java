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

package consulo.xstylesheet.definition.value.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import consulo.ui.shared.ColorValue;
import consulo.ui.util.ColorValueUtil;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class ColorXStyleSheetValue extends TextBasedXStyleSheetPropertyValuePartParser
{
	@Nullable
	@Override
	public ColorValue fromString(@Nonnull String stringValue, String value)
	{
		try
		{
			return ColorValueUtil.fromHex(stringValue);
		}
		catch(Exception e)
		{
			return null;
		}
	}
}