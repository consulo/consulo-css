package org.consulo.xstylesheet.definition.value.impl;

import java.util.Collections;
import java.util.List;

import org.consulo.xstylesheet.highlight.XStyleSheetColors;
import org.consulo.xstylesheet.psi.PsiXStyleSheetPropertyValuePart;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.codeInsight.daemon.impl.HighlightInfoType;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class KeywordXStyleSheetValue extends TextBasedXStyleSheetPropertyValuePartParser
{
	@Nullable
	@Override
	public String fromString(@NotNull String stringValue, String value)
	{
		if(stringValue.equals(value))
		{
			return stringValue;
		}
		return null;
	}

	@NotNull
	@Override
	public List<HighlightInfo> createHighlights(@NotNull PsiXStyleSheetPropertyValuePart valuePart)
	{
		HighlightInfo.Builder builder = HighlightInfo.newHighlightInfo(HighlightInfoType.INFORMATION);
		builder.textAttributes(XStyleSheetColors.KEYWORD);
		builder.range(valuePart);
		return Collections.singletonList(builder.create());
	}

	@NotNull
	@Override
	public List<LookupElement> getLookupElements(String value)
	{
		LookupElementBuilder builder = LookupElementBuilder.create(value);
		builder = builder.withBoldness(true);
		return Collections.<LookupElement>singletonList(builder);
	}
}
