package consulo.xstylesheet.psi;

import consulo.language.psi.PsiFile;
import consulo.util.collection.ContainerUtil;
import consulo.xstylesheet.definition.XStyleSheetTable;
import consulo.xstylesheet.definition.XStyleSheetTableProvider;
import consulo.xstylesheet.definition.impl.EmptyXStyleSheetTable;
import consulo.xstylesheet.definition.impl.MergedXStyleSheetTable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author VISTALL
 * @since 05.12.2015
 */
public interface XStyleSheetFile extends PsiFile
{
	@Nonnull
	static XStyleSheetTable getXStyleSheetTable(@Nonnull PsiFile file)
	{
		if(!(file instanceof XStyleSheetFile))
		{
			return EmptyXStyleSheetTable.INSTANCE;
		}

		List<XStyleSheetTable> list = new ArrayList<>();
		for(XStyleSheetTableProvider extension : XStyleSheetTableProvider.EP_NAME.getExtensionList())
		{
			ContainerUtil.addIfNotNull(list, extension.getTableForFile((XStyleSheetFile) file));
		}
		return list.isEmpty() ? EmptyXStyleSheetTable.INSTANCE : new MergedXStyleSheetTable(list.toArray(new XStyleSheetTable[list.size()]));
	}

	@Nonnull
	XStyleSheetRoot getRoot();
}
