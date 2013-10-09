package org.consulo.xstylesheet.definition.value.impl;

import java.util.Collections;
import java.util.List;

import org.consulo.xstylesheet.definition.XStyleSheetPropertyValuePartParser;
import org.consulo.xstylesheet.psi.PsiXStyleSheetPropertyValuePart;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.codeInsight.lookup.LookupElement;

/**
 * @author VISTALL
 * @since 09.10.13.
 */
public class BaseXStyleSheetPropertyValuePartParser implements XStyleSheetPropertyValuePartParser
{
	@Nullable
	@Override
	public HighlightInfo createHighlightInfo(@NotNull PsiXStyleSheetPropertyValuePart valuePart)
	{
		return null;
	}

	@Nullable
	@Override
	public Object getNativeValue(@NotNull PsiXStyleSheetPropertyValuePart valuePart, String value)
	{
		return null;
	}

	@NotNull
	@Override
	public List<LookupElement> getLookupElements(String value)
	{
		return Collections.emptyList();
	}
}
