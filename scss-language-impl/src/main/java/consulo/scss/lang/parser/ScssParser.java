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

import consulo.css.lang.CssTokens;
import consulo.css.lang.parser.CssParser;
import consulo.css.localize.CssLocalize;
import consulo.language.ast.IElementType;
import consulo.language.parser.PsiBuilder;
import consulo.scss.lang.ScssElements;
import consulo.scss.lang.ScssTokens;

/**
 * SCSS parser extending CssParser. Overridden methods use ScssElements explicitly
 * to produce SCSS-language-bound PSI nodes. Inherited methods from CssParser will
 * use CssElements (CssLanguage-bound), which is fine since SCSS isKindOf CSS.
 *
 * @author VISTALL
 * @since 2026-03-16
 */
public class ScssParser extends CssParser {
    @Override
    public void parseRoot(PsiBuilder builder) {
        PsiBuilder.Marker rootMarker = builder.mark();
        while (!builder.eof()) {
            if (!parseRootItem(builder)) {
                markError(builder, CssLocalize.unexpectedToken());
            }
        }
        rootMarker.done(ScssElements.ROOT);
    }

    @Override
    protected boolean parseRootItem(PsiBuilder builder) {
        IElementType type = builder.getTokenType();

        if (type == ScssTokens.MIXIN_KEYWORD) {
            return parseMixin(builder);
        }
        else if (type == ScssTokens.INCLUDE_KEYWORD) {
            return parseInclude(builder);
        }
        else if (type == ScssTokens.USE_KEYWORD) {
            return parseUseOrForward(builder);
        }
        else if (type == ScssTokens.FORWARD_KEYWORD) {
            return parseUseOrForward(builder);
        }
        else if (type == ScssTokens.DOLLAR) {
            return parseScssVariableDeclaration(builder);
        }

        return super.parseRootItem(builder);
    }

    @Override
    protected void parsePropertyBlock(PsiBuilder builder) {
        if (builder.getTokenType() == CssTokens.LBRACE) {
            PsiBuilder.Marker bodyMarker = builder.mark();
            builder.advanceLexer();

            while (!builder.eof()) {
                IElementType type = builder.getTokenType();

                if (type == CssTokens.RBRACE) {
                    break;
                }

                // SCSS variable declaration: $name: value;
                if (type == ScssTokens.DOLLAR) {
                    if (parseScssVariableDeclaration(builder)) {
                        continue;
                    }
                }

                // SCSS @include inside block
                if (type == ScssTokens.INCLUDE_KEYWORD) {
                    if (parseInclude(builder)) {
                        continue;
                    }
                }

                // Decide between property and nested rule using lookahead:
                // If IDENTIFIER is followed by COLON, it's a property (e.g., "color: red;")
                // Otherwise, it's a nested rule (e.g., "ul { ... }" or ".class { ... }")
                if (type == CssTokens.IDENTIFIER && builder.lookAhead(1) == CssTokens.COLON) {
                    if (parseProperty(builder, null) != null) {
                        continue;
                    }
                }

                // Try parsing as a nested rule (SCSS nesting)
                if (parseRule(builder)) {
                    continue;
                }

                // Fallback: try parsing as a property (for non-IDENTIFIER property starts like *)
                if (parseProperty(builder, null) != null) {
                    continue;
                }

                break;
            }

            expect(builder, CssTokens.RBRACE, CssLocalize.expectedRbrace());

            bodyMarker.done(ScssElements.BLOCK);
        }
        else {
            builder.error(CssLocalize.expectedLbrace());
        }
    }

    @Override
    protected boolean parseSimpleSelector(PsiBuilder builder) {
        // Handle & parent selector
        if (builder.getTokenType() == ScssTokens.AMPERSAND) {
            PsiBuilder.Marker marker = builder.mark();
            builder.advanceLexer();

            // & can be followed by suffix like &-item, &__element, &:hover
            PsiBuilder.Marker suffixListMarker = builder.mark();
            parseSelectorAttributeList(builder);
            parseSelectorPseudoClass(builder);
            suffixListMarker.done(ScssElements.SELECTOR_SUFFIX_LIST);

            marker.done(ScssElements.SIMPLE_SELECTOR);
            return true;
        }

        return super.parseSimpleSelector(builder);
    }

    @Override
    public void parsePropertyValue(PsiBuilder builder) {
        while (!builder.eof()) {
            IElementType type = builder.getTokenType();
            if (type == CssTokens.COMMA || type == CssTokens.SLASH) {
                builder.advanceLexer();
            }
            else if (type == CssTokens.SEMICOLON || type == CssTokens.RBRACE || type == CssTokens.IMPORTANT_KEYWORD) {
                break;
            }
            else if (type == ScssTokens.DOLLAR) {
                // SCSS variable reference in value: $variable-name
                PsiBuilder.Marker valueMarker = builder.mark();
                PsiBuilder.Marker varMarker = builder.mark();
                builder.advanceLexer(); // consume $

                if (builder.getTokenType() == CssTokens.IDENTIFIER) {
                    builder.advanceLexer();
                    varMarker.done(ScssElements.SCSS_VARIABLE_REFERENCE);
                }
                else {
                    varMarker.drop();
                }

                valueMarker.done(ScssElements.PROPERTY_VALUE_PART);
            }
            else {
                PsiBuilder.Marker valueMarker = builder.mark();

                if (parseFunctionCall(builder)) {
                    valueMarker.done(ScssElements.PROPERTY_VALUE_PART);
                    continue;
                }

                if (type == CssTokens.SHARP) {
                    int rawOffset = 1;
                    while (true) {
                        IElementType raw = builder.rawLookup(rawOffset);
                        if (raw == CssTokens.NUMBER || raw == CssTokens.IDENTIFIER) {
                            rawOffset++;
                        }
                        else {
                            break;
                        }
                    }

                    if (rawOffset > 1) {
                        PsiBuilder.Marker colorMark = builder.mark();
                        for (int i = 0; i < rawOffset; i++) {
                            builder.advanceLexer();
                        }
                        colorMark.collapse(CssTokens.NUMBER);
                    }
                    else {
                        builder.advanceLexer();
                    }
                }
                else if (type == CssTokens.NUMBER) {
                    IElementType nextRaw = builder.rawLookup(1);
                    if (nextRaw == CssTokens.IDENTIFIER) {
                        PsiBuilder.Marker collapseMark = builder.mark();
                        builder.advanceLexer();
                        builder.advanceLexer();
                        collapseMark.collapse(CssTokens.NUMBER);
                    }
                    else {
                        builder.advanceLexer();
                    }
                }
                else if (type == CssTokens.BAD_CHARACTER) {
                    PsiBuilder.Marker mark = builder.mark();
                    builder.advanceLexer();
                    mark.error(CssLocalize.badToken());
                }
                else {
                    builder.advanceLexer();
                }

                valueMarker.done(ScssElements.PROPERTY_VALUE_PART);
            }
        }
    }

    private boolean parseScssVariableDeclaration(PsiBuilder builder) {
        if (builder.getTokenType() != ScssTokens.DOLLAR) {
            return false;
        }

        PsiBuilder.Marker marker = builder.mark();
        builder.advanceLexer(); // consume $

        if (builder.getTokenType() != CssTokens.IDENTIFIER) {
            marker.rollbackTo();
            return false;
        }

        builder.advanceLexer(); // consume name

        if (builder.getTokenType() != CssTokens.COLON) {
            marker.rollbackTo();
            return false;
        }

        builder.advanceLexer(); // consume :

        parsePropertyValue(builder);

        if (builder.getTokenType() == CssTokens.IMPORTANT_KEYWORD) {
            builder.advanceLexer();
        }

        boolean last = builder.getTokenType() == null || builder.getTokenType() == CssTokens.RBRACE;

        if (last) {
            optional(builder, CssTokens.SEMICOLON);
        }
        else {
            expect(builder, CssTokens.SEMICOLON, CssLocalize.expectedSemicolon());
        }

        marker.done(ScssElements.SCSS_VARIABLE);
        return true;
    }

    private boolean parseMixin(PsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();
        builder.advanceLexer(); // consume @mixin

        expect(builder, CssTokens.IDENTIFIER, CssLocalize.expectedIdentifier());

        // Optional parameter list
        if (builder.getTokenType() == CssTokens.LPAR) {
            builder.advanceLexer();
            // Consume parameters (simplified: just eat everything until RPAR)
            while (!builder.eof() && builder.getTokenType() != CssTokens.RPAR) {
                builder.advanceLexer();
            }
            optional(builder, CssTokens.RPAR);
        }

        parsePropertyBlock(builder);

        marker.done(ScssElements.MIXIN);
        return true;
    }

    private boolean parseInclude(PsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();
        builder.advanceLexer(); // consume @include

        expect(builder, CssTokens.IDENTIFIER, CssLocalize.expectedIdentifier());

        // Optional argument list
        if (builder.getTokenType() == CssTokens.LPAR) {
            builder.advanceLexer();
            while (!builder.eof() && builder.getTokenType() != CssTokens.RPAR) {
                builder.advanceLexer();
            }
            optional(builder, CssTokens.RPAR);
        }

        // @include can be followed by a block or semicolon
        if (builder.getTokenType() == CssTokens.LBRACE) {
            parsePropertyBlock(builder);
        }
        else {
            boolean last = builder.getTokenType() == null || builder.getTokenType() == CssTokens.RBRACE;
            if (last) {
                optional(builder, CssTokens.SEMICOLON);
            }
            else {
                expect(builder, CssTokens.SEMICOLON, CssLocalize.expectedSemicolon());
            }
        }

        marker.done(ScssElements.INCLUDE);
        return true;
    }

    private boolean parseUseOrForward(PsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();
        builder.advanceLexer(); // consume @use or @forward

        if (builder.getTokenType() == CssTokens.STRING) {
            builder.advanceLexer();
        }
        else {
            builder.error(CssLocalize.expectedImportUrl());
        }

        // @use 'file' as prefix; or @use 'file' with (...)
        while (!builder.eof() && builder.getTokenType() != CssTokens.SEMICOLON) {
            builder.advanceLexer();
        }

        optional(builder, CssTokens.SEMICOLON);

        marker.done(ScssElements.IMPORT);
        return true;
    }
}
