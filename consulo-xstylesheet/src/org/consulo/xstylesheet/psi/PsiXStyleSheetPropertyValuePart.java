package org.consulo.xstylesheet.psi;

import com.intellij.psi.PsiElement;
import org.consulo.xstylesheet.definition.XStyleSheetPropertyValuePart;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public interface PsiXStyleSheetPropertyValuePart extends PsiElement {
  Object getValue();

  XStyleSheetPropertyValuePart getValuePart();

  XStyleSheetPropertyValuePart[] getValueParts();
}
