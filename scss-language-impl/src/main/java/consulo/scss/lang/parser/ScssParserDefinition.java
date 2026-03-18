/*
 * Copyright 2013-2026 consulo.io
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

package consulo.scss.lang.parser;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.ast.IFileElementType;
import consulo.language.ast.TokenSet;
import consulo.language.file.FileViewProvider;
import consulo.language.lexer.Lexer;
import consulo.language.parser.ParserDefinition;
import consulo.language.parser.PsiParser;
import consulo.language.psi.PsiFile;
import consulo.language.version.LanguageVersion;
import consulo.scss.lang.ScssLanguage;
import consulo.scss.lang.ScssTokenSets;
import consulo.scss.lang.lexer._ScssLexer;
import consulo.scss.lang.psi.ScssFile;

/**
 * @author VISTALL
 * @since 2026-03-16
 */
@ExtensionImpl
public class ScssParserDefinition implements ParserDefinition {
    public static final IFileElementType FILE_ELEMENT = new IFileElementType("SCSS_FILE", ScssLanguage.INSTANCE);

    @Override
    public Language getLanguage() {
        return ScssLanguage.INSTANCE;
    }

    @Override
    public Lexer createLexer(LanguageVersion languageVersion) {
        return new _ScssLexer();
    }

    @Override
    public PsiParser createParser(LanguageVersion languageVersion) {
        return new ScssParser();
    }

    @Override
    public IFileElementType getFileNodeType() {
        return FILE_ELEMENT;
    }

    @Override
    public TokenSet getWhitespaceTokens(LanguageVersion languageVersion) {
        return ScssTokenSets.WHITE_SPACES;
    }

    @Override
    public TokenSet getCommentTokens(LanguageVersion languageVersion) {
        return ScssTokenSets.COMMENTS;
    }

    @Override
    public TokenSet getStringLiteralElements(LanguageVersion languageVersion) {
        return ScssTokenSets.STRINGS;
    }

    @Override
    public PsiFile createFile(FileViewProvider fileViewProvider) {
        return new ScssFile(fileViewProvider);
    }
}
