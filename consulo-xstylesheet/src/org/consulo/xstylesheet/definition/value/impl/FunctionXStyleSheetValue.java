package org.consulo.xstylesheet.definition.value.impl;

import java.util.Collections;
import java.util.List;

import org.consulo.xstylesheet.psi.PsiXStyleSheetPropertyValuePart;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.psi.impl.DebugUtil;

/**
 * @author VISTALL
 * @since 09.10.13.
 */
public class FunctionXStyleSheetValue extends BaseXStyleSheetPropertyValuePartParser
{
	@Nullable
	@Override
	public HighlightInfo createHighlightInfo(@NotNull PsiXStyleSheetPropertyValuePart valuePart)
	{
		System.out.println(DebugUtil.psiToString(valuePart, false));
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
