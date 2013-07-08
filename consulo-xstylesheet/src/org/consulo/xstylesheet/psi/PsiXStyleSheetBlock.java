package org.consulo.xstylesheet.psi;

/**
 * @author VISTALL
 * @since 08.07.13.
 */
public interface PsiXStyleSheetBlock extends PsiXStyleSheetElement {
  PsiXStyleSheetProperty[] getProperties();
}
