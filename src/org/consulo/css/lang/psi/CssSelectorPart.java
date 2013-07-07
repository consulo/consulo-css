package org.consulo.css.lang.psi;

import com.intellij.psi.PsiElement;

/**
 * @author VISTALL
 * @since 08.07.13.
 */
public interface CssSelectorPart extends PsiElement{
  String getOnlyName();
}
