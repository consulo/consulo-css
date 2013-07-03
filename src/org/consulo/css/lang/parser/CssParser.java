package org.consulo.css.lang.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.LanguageVersion;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class CssParser implements PsiParser {
    @NotNull
    @Override
    public ASTNode parse(@NotNull IElementType iElementType, @NotNull PsiBuilder psiBuilder, @NotNull LanguageVersion languageVersion) {
        PsiBuilder.Marker mark = psiBuilder.mark();
        while (!psiBuilder.eof()) {
            psiBuilder.advanceLexer();
        }
        mark.done(iElementType);
        return psiBuilder.getTreeBuilt();
    }
}
