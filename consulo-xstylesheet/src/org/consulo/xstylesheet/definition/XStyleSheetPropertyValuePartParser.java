package org.consulo.xstylesheet.definition;

import java.util.List;

import org.consulo.xstylesheet.psi.PsiXStyleSheetPropertyValuePart;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.codeInsight.lookup.LookupElement;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public interface XStyleSheetPropertyValuePartParser
{
	@NotNull
	List<HighlightInfo> createHighlights(@NotNull PsiXStyleSheetPropertyValuePart valuePart);

	@Nullable
	Object getNativeValue(@NotNull PsiXStyleSheetPropertyValuePart valuePart, String value);

	@NotNull
	List<LookupElement> getLookupElements(String value);
}
