package org.consulo.css.lang.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.util.SmartList;
import org.consulo.css.lang.CssFileType;
import org.consulo.css.lang.CssLanguage;
import org.consulo.xstylesheet.definition.XStyleSheetTable;
import org.consulo.xstylesheet.definition.XStyleSheetTableExtension;
import org.consulo.xstylesheet.definition.impl.EmptyXStyleSheetTable;
import org.consulo.xstylesheet.definition.impl.MergedXStyleSheetTable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author VISTALL
 * @since 02.07.13.
 */
public class CssFile extends PsiFileBase {
  public CssFile(@NotNull FileViewProvider provider) {
    super(provider, CssLanguage.INSTANCE);
  }

  @NotNull
  @Override
  public FileType getFileType() {
    return CssFileType.INSTANCE;
  }

  @Override
  public void accept(@NotNull PsiElementVisitor psiElementVisitor) {
    psiElementVisitor.visitFile(this);
  }

  @NotNull
  public XStyleSheetTable getXStyleSheetTable() {
    SmartList<XStyleSheetTable> list = new SmartList<XStyleSheetTable>();
    for (XStyleSheetTableExtension extension : XStyleSheetTableExtension.EP_NAME.getExtensions()) {
      /*if (extension.condition == null || extension.condition.value(this)) */{
        list.add(extension.getTable());
      }
    }
    return list.isEmpty() ? EmptyXStyleSheetTable.INSTANCE : new MergedXStyleSheetTable(list.toArray(new XStyleSheetTable[list.size()]));
  }
}
