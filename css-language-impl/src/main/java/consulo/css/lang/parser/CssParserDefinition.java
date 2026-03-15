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

package consulo.css.lang.parser;

import consulo.annotation.component.ExtensionImpl;
import consulo.css.lang.CssLanguage;
import consulo.css.lang.CssTokenSets;
import consulo.css.lang.lexer._CssLexer;
import consulo.css.lang.psi.CssFile;
import consulo.language.Language;
import consulo.language.ast.IFileElementType;
import consulo.language.ast.TokenSet;
import consulo.language.file.FileViewProvider;
import consulo.language.lexer.Lexer;
import consulo.language.parser.ParserDefinition;
import consulo.language.parser.PsiParser;
import consulo.language.psi.PsiFile;
import consulo.language.version.LanguageVersion;


/**
 * @author VISTALL
 * @since 2013-06-12
 */
@ExtensionImpl
public class CssParserDefinition implements ParserDefinition {
    public static final IFileElementType FILE_ELEMENT = new IFileElementType("CSS_FILE", CssLanguage.INSTANCE);

    @Override
    public Language getLanguage() {
        return CssLanguage.INSTANCE;
    }

    @Override
    public Lexer createLexer(LanguageVersion languageVersion) {
        return new _CssLexer();
    }

    @Override
    public PsiParser createParser(LanguageVersion languageVersion) {
        return new CssParser();
    }

    @Override
    public IFileElementType getFileNodeType() {
        return FILE_ELEMENT;
    }

    @Override
    public TokenSet getWhitespaceTokens(LanguageVersion languageVersion) {
        return CssTokenSets.WHITE_SPACES;
    }

    @Override
    public TokenSet getCommentTokens(LanguageVersion languageVersion) {
        return CssTokenSets.COMMENTS;
    }

    @Override
    public TokenSet getStringLiteralElements(LanguageVersion languageVersion) {
        return CssTokenSets.STRINGS;
    }

    @Override
    public PsiFile createFile(FileViewProvider fileViewProvider) {
        return new CssFile(fileViewProvider);
    }
}
