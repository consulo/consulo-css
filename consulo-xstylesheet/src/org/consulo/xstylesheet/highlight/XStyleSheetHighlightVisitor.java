package org.consulo.xstylesheet.highlight;

import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.codeInsight.daemon.impl.HighlightInfoType;
import com.intellij.codeInsight.daemon.impl.HighlightVisitor;
import com.intellij.codeInsight.daemon.impl.analysis.HighlightInfoHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import org.consulo.xstylesheet.definition.XStyleSheetPropertyValuePart;
import org.consulo.xstylesheet.definition.XStyleSheetPropertyValuePartParser;
import org.consulo.xstylesheet.definition.value.impl.KeywordXStyleSheetValue;
import org.consulo.xstylesheet.definition.value.impl.LikeXStyleSheetPropertyValuePartParser;
import org.consulo.xstylesheet.psi.PsiXStyleSheetProperty;
import org.consulo.xstylesheet.psi.PsiXStyleSheetPropertyValuePart;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class XStyleSheetHighlightVisitor implements HighlightVisitor {
  private HighlightInfoHolder myHighlightInfoHolder;

  @Override
  public boolean suitableForFile(@NotNull PsiFile psiFile) {
    return true;
  }

  @Override
  public void visit(@NotNull PsiElement psiElement) {
    psiElement.accept(new PsiElementVisitor() {
      @Override
      public void visitElement(PsiElement element) {
        if(element instanceof PsiXStyleSheetProperty) {
          PsiElement nameIdentifier = ((PsiXStyleSheetProperty) element).getNameIdentifier();
          if(nameIdentifier != null) {
            HighlightInfo.Builder builder = HighlightInfo.newHighlightInfo(HighlightInfoType.INFORMATION);
            builder.textAttributes(XStyleSheetColors.PROPERTY_NAME);
            builder.range(nameIdentifier);

            myHighlightInfoHolder.add(builder.create());
          }
        }
        else if(element instanceof PsiXStyleSheetPropertyValuePart) {
          XStyleSheetPropertyValuePart valuePart = ((PsiXStyleSheetPropertyValuePart) element).getValuePart();
          if(valuePart != null) {
            XStyleSheetPropertyValuePartParser parser = valuePart.getParser();
            if(parser instanceof LikeXStyleSheetPropertyValuePartParser) {
              HighlightInfo.Builder builder = HighlightInfo.newHighlightInfo(HighlightInfoType.INFORMATION);
              builder.textAttributes(XStyleSheetColors.KEYWORD);
              builder.range(element);

              myHighlightInfoHolder.add(builder.create());
            }
          }
        }
        element.acceptChildren(this);
      }
    });
  }

  @Override
  public boolean analyze(@NotNull PsiFile psiFile, boolean b, @NotNull HighlightInfoHolder highlightInfoHolder, @NotNull Runnable runnable) {
    myHighlightInfoHolder = highlightInfoHolder;
    runnable.run();
    return true;
  }

  @NotNull
  @Override
  public HighlightVisitor clone() {
    return new XStyleSheetHighlightVisitor();
  }

  @Override
  public int order() {
    return 0;
  }
}
