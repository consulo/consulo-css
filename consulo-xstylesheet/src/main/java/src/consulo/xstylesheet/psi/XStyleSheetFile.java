package consulo.xstylesheet.psi;

import org.jetbrains.annotations.NotNull;
import com.intellij.psi.PsiFile;
import com.intellij.util.SmartList;
import consulo.xstylesheet.definition.XStyleSheetTable;
import consulo.xstylesheet.definition.XStyleSheetTableExtension;
import consulo.xstylesheet.definition.impl.EmptyXStyleSheetTable;
import consulo.xstylesheet.definition.impl.MergedXStyleSheetTable;

/**
 * @author VISTALL
 * @since 05.12.2015
 */
public interface XStyleSheetFile extends PsiFile
{
	@NotNull
	static XStyleSheetTable getXStyleSheetTable(@NotNull PsiFile file)
	{
		SmartList<XStyleSheetTable> list = new SmartList<>();
		for(XStyleSheetTableExtension extension : XStyleSheetTableExtension.EP_NAME.getExtensions())
		{
	  /*if (extension.condition == null || extension.condition.value(this)) */
			{
				list.add(extension.getTable());
			}
		}
		return list.isEmpty() ? EmptyXStyleSheetTable.INSTANCE : new MergedXStyleSheetTable(list.toArray(new XStyleSheetTable[list.size()]));
	}

	@NotNull
	XStyleSheetRoot getRoot();
}
