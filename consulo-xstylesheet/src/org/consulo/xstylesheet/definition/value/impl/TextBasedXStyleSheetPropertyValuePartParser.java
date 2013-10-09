package org.consulo.xstylesheet.definition.value.impl;

import org.consulo.xstylesheet.psi.PsiXStyleSheetPropertyValuePart;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author VISTALL
 * @since 09.10.13.
 */
public abstract class TextBasedXStyleSheetPropertyValuePartParser extends BaseXStyleSheetPropertyValuePartParser
{
	@Nullable
	@Override
	public Object getNativeValue(@NotNull PsiXStyleSheetPropertyValuePart valuePart, String value)
	{
		return fromString(valuePart.getText().trim(), value);
	}

	public abstract Object fromString(@NotNull String valuePart, String value);
}
