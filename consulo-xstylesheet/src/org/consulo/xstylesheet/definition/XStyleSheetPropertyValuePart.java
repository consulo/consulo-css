package org.consulo.xstylesheet.definition;

import java.util.List;

import org.consulo.xstylesheet.psi.PsiXStyleSheetPropertyValuePart;
import org.jetbrains.annotations.NotNull;
import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.codeInsight.lookup.LookupElement;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public interface XStyleSheetPropertyValuePart
{
	XStyleSheetPropertyValuePart[] EMPTY_ARRAY = new XStyleSheetPropertyValuePart[0];

	XStyleSheetPropertyValuePartParser getParser();

	@NotNull
	List<HighlightInfo> createHighlights(@NotNull PsiXStyleSheetPropertyValuePart valuePart);

	String getValue();

	Object getNativeValue(PsiXStyleSheetPropertyValuePart part);

	List<LookupElement> getLookupElements();
}
