/*
 * Copyright 2013-2017 must-be.org
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

package consulo.css.html.psi;

import consulo.css.html.lexer.CssInlineLexer;
import consulo.css.lang.CssElements;
import consulo.css.lang.CssTokens;
import consulo.css.lang.parser.CssParser;
import consulo.css.localize.CssLocalize;
import consulo.language.Language;
import consulo.language.ast.ASTNode;
import consulo.language.ast.ILazyParseableElementType;
import consulo.language.lexer.Lexer;
import consulo.language.parser.PsiBuilder;
import consulo.language.parser.PsiBuilderFactory;
import consulo.language.psi.PsiElement;
import consulo.language.version.LanguageVersion;
import consulo.project.Project;
import consulo.xml.language.psi.XmlAttributeValue;
import consulo.xml.language.psi.XmlTag;

/**
 * A stylesheet embedded into HTML: the content of {@code <style>} (a stylesheet) or of a {@code style} attribute (the
 * declarations of an inline rule). A CSS dialect embeds its stylesheets with its own lexer and parser.
 */
public class CssStylesheetLazyElementType extends ILazyParseableElementType {
    public CssStylesheetLazyElementType(String debugName, Language language) {
        super(debugName, language);
    }

    protected Lexer createLexer() {
        return new CssInlineLexer();
    }

    protected CssParser createParser() {
        return new CssParser();
    }

    @Override
    protected ASTNode doParseContents(ASTNode chameleon, PsiElement psi) {
        final Project project = psi.getProject();
        final Language languageForParser = getLanguageForParser(psi);
        final LanguageVersion tempLanguageVersion = chameleon.getUserData(LanguageVersion.KEY);
        final LanguageVersion languageVersion = tempLanguageVersion == null ? psi.getLanguageVersion() : tempLanguageVersion;
        Lexer lexer = createLexer();

        final PsiBuilder builder = PsiBuilderFactory.getInstance()
            .createBuilder(project, chameleon, lexer, languageForParser, languageVersion, chameleon.getChars());
        final CssParser parser = createParser();

        boolean inline = isInline(psi, parser, builder);

        if (inline) {
            PsiBuilder.Marker rootMark = builder.mark();
            parseInlineRule(parser, builder);
            rootMark.done(CssElements.ROOT);
        }
        else {
            parser.parseRoot(builder);
        }

        return builder.getTreeBuilt();
    }

    /**
     * The content of a {@code style} attribute is the declarations of an inline rule, the content of a {@code <style>}
     * tag is a stylesheet; elsewhere a stylesheet starts with a selector list and a block.
     */
    private static boolean isInline(PsiElement parent, CssParser parser, PsiBuilder builder) {
        if (parent instanceof XmlAttributeValue) {
            return true;
        }
        if (parent instanceof XmlTag) {
            return false;
        }

        PsiBuilder.Marker mark = builder.mark();
        boolean inline = !(parser.parseSelectorListNew(builder) && builder.getTokenType() == CssTokens.LBRACE);
        mark.rollbackTo();
        return inline;
    }

    private void parseInlineRule(CssParser cssParser, PsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();

        PsiBuilder.Marker bodyMarker = builder.mark();

        while (!builder.eof()) {
            if (builder.getTokenType() == CssTokens.IDENTIFIER) {
                PsiBuilder.Marker propertyMarker = builder.mark();

                builder.advanceLexer();

                if (CssParser.expect(builder, CssTokens.COLON, CssLocalize.expectedColon())) {
                    cssParser.parsePropertyValue(builder);
                }

                if (builder.lookAhead(1) == CssTokens.IDENTIFIER) {
                    CssParser.expect(builder, CssTokens.SEMICOLON, CssLocalize.expectedSemicolon());
                }
                else {
                    CssParser.optional(builder, CssTokens.SEMICOLON);
                }

                propertyMarker.done(CssElements.PROPERTY);
            }
            else {
                builder.error(CssLocalize.unexpectedToken());
                builder.advanceLexer();
            }
        }

        bodyMarker.done(CssElements.BLOCK);

        marker.done(CssElements.RULE);
    }

    @Override
    protected Language getLanguageForParser(PsiElement psi) {
        return getLanguage();
    }
}
