package consulo.xstylesheet.psi;

import consulo.language.psi.PsiFile;
import consulo.util.collection.ContainerUtil;
import consulo.xstylesheet.definition.XStyleSheetTable;
import consulo.xstylesheet.definition.XStyleSheetTableProvider;
import consulo.xstylesheet.definition.impl.EmptyXStyleSheetTable;
import consulo.xstylesheet.definition.impl.MergedXStyleSheetTable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author VISTALL
 * @since 2015-12-05
 */
public interface XStyleSheetFile extends PsiFile {
    static XStyleSheetTable getXStyleSheetTable(PsiFile file) {
        List<XStyleSheetTable> list = new ArrayList<>();
        XStyleSheetTableProvider.EP_NAME.forEachExtensionSafe(
            extension -> ContainerUtil.addIfNotNull(list, extension.getTableForFile(file))
        );

        return list.isEmpty()
            ? EmptyXStyleSheetTable.INSTANCE
            : new MergedXStyleSheetTable(list.toArray(new XStyleSheetTable[list.size()]));
    }

    XStyleSheetRoot getRoot();
}
