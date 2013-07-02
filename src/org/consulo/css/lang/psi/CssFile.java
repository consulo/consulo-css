package org.consulo.css.lang.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.impl.source.PsiFileImpl;
import org.consulo.css.lang.CssFileType;
import org.consulo.css.lang.CssLanguage;
import org.jetbrains.annotations.NotNull;

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
}
