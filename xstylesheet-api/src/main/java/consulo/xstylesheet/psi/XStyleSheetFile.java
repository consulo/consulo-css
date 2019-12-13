package consulo.xstylesheet.psi;

import com.intellij.psi.PsiFile;
import com.intellij.util.SmartList;
import consulo.xstylesheet.definition.XStyleSheetTable;
import consulo.xstylesheet.definition.XStyleSheetTableExtension;
import consulo.xstylesheet.definition.impl.EmptyXStyleSheetTable;
import consulo.xstylesheet.definition.impl.MergedXStyleSheetTable;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 05.12.2015
 */
public interface XStyleSheetFile extends PsiFile
{
	@Nonnull
	static XStyleSheetTable getXStyleSheetTable(@Nonnull PsiFile file)
	{
		SmartList<XStyleSheetTable> list = new SmartList<>();
		for(XStyleSheetTableExtension extension : XStyleSheetTableExtension.EP_NAME.getExtensionList())
		{
	  /*if (extension.condition == null || extension.condition.value(this)) */
			{
				list.add(extension.getTable());
			}
		}
		return list.isEmpty() ? EmptyXStyleSheetTable.INSTANCE : new MergedXStyleSheetTable(list.toArray(new XStyleSheetTable[list.size()]));
	}

	@Nonnull
	XStyleSheetRoot getRoot();
}
