package org.consulo.css.lang;

import com.intellij.openapi.fileTypes.SingleLazyInstanceSyntaxHighlighterFactory;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class CssSyntaxHighlighterFactory extends SingleLazyInstanceSyntaxHighlighterFactory {
  @NotNull
  @Override
  protected SyntaxHighlighter createHighlighter() {
    return new CssSyntaxHighlighter();
  }
}
