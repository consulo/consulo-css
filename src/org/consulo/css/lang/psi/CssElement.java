package org.consulo.css.lang.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public abstract class CssElement extends ASTWrapperPsiElement {
  public CssElement(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public CssFile getContainingFile() {
    return (CssFile) super.getContainingFile();
  }
}
