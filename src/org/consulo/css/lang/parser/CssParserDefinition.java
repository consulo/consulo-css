package org.consulo.css.lang.parser;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.*;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.impl.source.PsiFileImpl;
import com.intellij.psi.tree.IElementType;
import org.consulo.css.lang.CssLanguage;
import org.consulo.css.lang.CssPsiElementType;
import org.consulo.css.lang.CssTokenSets;
import org.consulo.css.lang.lexer.CssLexer;
import org.consulo.css.lang.psi.CssFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;

/**
 * @author VISTALL
 * @since 23:59/12.06.13
 */
public class CssParserDefinition implements ParserDefinition {
    private static final IFileElementType FILE_ELEMENT = new IFileElementType("CSS_FILE", CssLanguage.INSTANCE);

    @NotNull
    @Override
    public Lexer createLexer(@Nullable Project project, @NotNull LanguageVersion languageVersion) {
        return new CssLexer();
    }

    @NotNull
    @Override
    public PsiParser createParser(@Nullable Project project, @NotNull LanguageVersion languageVersion) {
        return new CssParser();
    }

    @NotNull
    @Override
    public IFileElementType getFileNodeType() {
        return FILE_ELEMENT;
    }

    @NotNull
    @Override
    public TokenSet getWhitespaceTokens(@NotNull LanguageVersion languageVersion) {
        return CssTokenSets.WHITE_SPACES;
    }

    @NotNull
    @Override
    public TokenSet getCommentTokens(@NotNull LanguageVersion languageVersion) {
        return CssTokenSets.COMMENTS;
    }

    @NotNull
    @Override
    public TokenSet getStringLiteralElements(@NotNull LanguageVersion languageVersion) {
        return CssTokenSets.STRINGS;
    }

    @NotNull
    @Override
    public PsiElement createElement(ASTNode astNode) {
        IElementType elementType = astNode.getElementType();
        if(elementType instanceof CssPsiElementType) {
            return ((CssPsiElementType) elementType).createPsi(astNode);
        }
        return new ASTWrapperPsiElement(astNode);
    }

    @Override
    public PsiFile createFile(FileViewProvider fileViewProvider) {
        return new CssFile(fileViewProvider);
    }

    @Override
    public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode astNode, ASTNode astNode2) {
        return SpaceRequirements.MAY;
    }
}
