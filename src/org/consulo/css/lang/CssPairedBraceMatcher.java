package org.consulo.css.lang;

import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author VISTALL
 * @since 08.07.13.
 */
public class CssPairedBraceMatcher implements PairedBraceMatcher {
  private BracePair[] myPairs = new BracePair[] {
      new BracePair(CssTokens.LBRACE, CssTokens.RBRACE, true),
      new BracePair(CssTokens.LBRACKET, CssTokens.RBRACKET, false),
  };

  @Override
  public BracePair[] getPairs() {
    return myPairs;
  }

  @Override
  public boolean isPairedBracesAllowedBeforeType(@NotNull IElementType elementType, @Nullable IElementType elementType2) {
    return false;
  }

  @Override
  public int getCodeConstructStart(PsiFile psiFile, int i) {
    return i;
  }
}
