package consulo.xstylesheet.codeInsight;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.rawHighlight.HighlightVisitor;
import consulo.language.editor.rawHighlight.HighlightVisitorFactory;
import consulo.language.psi.PsiFile;
import consulo.xml.psi.xml.XmlFile;
import consulo.xstylesheet.psi.XStyleSheetFile;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 2023-03-26
 */
@ExtensionImpl
public class XStyleSheetHighlightVisitorFactory implements HighlightVisitorFactory {
    @Override
    public boolean suitableForFile(@Nonnull PsiFile psiFile) {
        return psiFile instanceof XStyleSheetFile || psiFile instanceof XmlFile;
    }

    @Nonnull
    @Override
    public HighlightVisitor createVisitor() {
        return new XStyleSheetHighlightVisitor();
    }
}
