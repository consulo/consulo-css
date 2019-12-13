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

import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import consulo.css.lang.CssLanguage;
import consulo.css.lang.CssTokenSets;
import consulo.css.lang.lexer._CssLexer;
import consulo.css.lang.psi.CssFile;
import consulo.lang.LanguageVersion;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 23:59/12.06.13
 */
public class CssParserDefinition implements ParserDefinition
{
	public static final IFileElementType FILE_ELEMENT = new IFileElementType("CSS_FILE", CssLanguage.INSTANCE);

	@Nonnull
	@Override
	public Lexer createLexer(@Nonnull LanguageVersion languageVersion)
	{
		return new _CssLexer();
	}

	@Nonnull
	@Override
	public PsiParser createParser(@Nonnull LanguageVersion languageVersion)
	{
		return new CssParser();
	}

	@Nonnull
	@Override
	public IFileElementType getFileNodeType()
	{
		return FILE_ELEMENT;
	}

	@Nonnull
	@Override
	public TokenSet getWhitespaceTokens(@Nonnull LanguageVersion languageVersion)
	{
		return CssTokenSets.WHITE_SPACES;
	}

	@Nonnull
	@Override
	public TokenSet getCommentTokens(@Nonnull LanguageVersion languageVersion)
	{
		return CssTokenSets.COMMENTS;
	}

	@Nonnull
	@Override
	public TokenSet getStringLiteralElements(@Nonnull LanguageVersion languageVersion)
	{
		return CssTokenSets.STRINGS;
	}

	@Override
	public PsiFile createFile(@Nonnull FileViewProvider fileViewProvider)
	{
		return new CssFile(fileViewProvider);
	}
}
