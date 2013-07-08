package org.consulo.css.lang.formatting;

import com.intellij.formatting.FormattingModel;
import com.intellij.formatting.FormattingModelBuilder;
import com.intellij.formatting.FormattingModelProvider;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import org.consulo.css.lang.CssLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author VISTALL
 * @since 08.07.13.
 */
public class CssFormattingModelBuilder implements FormattingModelBuilder {
  @NotNull
  @Override
  public FormattingModel createModel(PsiElement psiElement, CodeStyleSettings codeStyleSettings) {
    ASTNode node = psiElement.getNode();
    assert node != null;
    PsiFile containingFile = psiElement.getContainingFile().getViewProvider().getPsi(CssLanguage.INSTANCE);
    assert containingFile != null : psiElement.getContainingFile();
    ASTNode fileNode = containingFile.getNode();
    assert fileNode != null;
    CssFormattingBlock block = new CssFormattingBlock(fileNode, null, null);
    return FormattingModelProvider.createFormattingModelForPsiFile(containingFile, block, codeStyleSettings);
  }

  @Nullable
  @Override
  public TextRange getRangeAffectingIndent(PsiFile psiFile, int i, ASTNode astNode) {
    return null;
  }
}
