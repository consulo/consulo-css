package consulo.xstylesheet.codeInsight;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.rawHighlight.HighlightVisitor;
import consulo.language.editor.rawHighlight.HighlightVisitorFactory;
import consulo.language.psi.PsiFile;
import consulo.xml.language.psi.XmlFile;
import consulo.xstylesheet.psi.XStyleSheetFile;


/**
 * @author VISTALL
 * @since 2023-03-26
 */
@ExtensionImpl
public class XStyleSheetHighlightVisitorFactory implements HighlightVisitorFactory {
    @Override
    public boolean suitableForFile(PsiFile psiFile) {
        return psiFile instanceof XStyleSheetFile || psiFile instanceof XmlFile;
    }

    @Override
    public HighlightVisitor createVisitor() {
        return new XStyleSheetHighlightVisitor();
    }
}
