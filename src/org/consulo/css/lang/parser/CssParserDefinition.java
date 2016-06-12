/*
 * Copyright 2013 must-be.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.consulo.css.lang.parser;

import org.consulo.css.lang.CssLanguage;
import org.consulo.css.lang.CssPsiElementType;
import org.consulo.css.lang.CssTokenSets;
import org.consulo.css.lang.lexer._CssLexer;
import org.consulo.css.lang.psi.CssFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.lang.LanguageVersion;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;

/**
 * @author VISTALL
 * @since 23:59/12.06.13
 */
public class CssParserDefinition implements ParserDefinition {
    public static final IFileElementType FILE_ELEMENT = new IFileElementType("CSS_FILE", CssLanguage.INSTANCE);

    @NotNull
    @Override
    public Lexer createLexer(@Nullable Project project, @NotNull LanguageVersion languageVersion) {
        return new _CssLexer();
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
