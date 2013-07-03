package org.consulo.css.editor.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.patterns.StandardPatterns;
import com.intellij.util.ProcessingContext;
import org.consulo.css.lang.psi.CssPropertyValuePart;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class CssCompletionContributor extends CompletionContributor {
  public CssCompletionContributor() {
    extend(CompletionType.BASIC, StandardPatterns.instanceOf(CssPropertyValuePart.class), new CompletionProvider<CompletionParameters>() {
      @Override
      protected void addCompletions(@NotNull CompletionParameters completionParameters, ProcessingContext processingContext, @NotNull CompletionResultSet completionResultSet) {
        System.out.println(completionParameters.getCompletionType());
      }
    });
  }
}
