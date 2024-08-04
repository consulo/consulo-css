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

import consulo.css.lang.CssElements;
import consulo.css.lang.CssTokenSets;
import consulo.css.lang.CssTokens;
import consulo.css.localize.CssLocalize;
import consulo.language.ast.ASTNode;
import consulo.language.ast.IElementType;
import consulo.language.parser.PsiBuilder;
import consulo.language.parser.PsiParser;
import consulo.language.version.LanguageVersion;
import consulo.localize.LocalizeValue;
import consulo.util.lang.StringUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * @author VISTALL
 * @since 2013-07-03
 */
public class CssParser implements PsiParser, CssTokens, CssElements {
    public static final String VAR_PREFIX = "--";

    public static boolean expect(PsiBuilder builder, IElementType elementType, @Nonnull LocalizeValue message) {
        boolean expectedToken = optional(builder, elementType);
        if (!expectedToken) {
            builder.error(message);
        }
        return expectedToken;
    }

    public static boolean optional(PsiBuilder builder, IElementType elementType) {
        boolean expectedToken = builder.getTokenType() == elementType;
        if (expectedToken) {
            builder.advanceLexer();
        }
        return expectedToken;
    }

    @Nonnull
    @Override
    public ASTNode parse(@Nonnull IElementType rootElementType, @Nonnull PsiBuilder builder, @Nonnull LanguageVersion languageVersion) {
        PsiBuilder.Marker mark = builder.mark();
        parseRoot(builder);
        mark.done(rootElementType);
        return builder.getTreeBuilt();
    }

    public void parseRoot(@Nonnull PsiBuilder builder) {
        PsiBuilder.Marker rootMarker = builder.mark();
        while (!builder.eof()) {
            PsiBuilder.Marker marker = builder.mark();

            if (builder.getTokenType() == CssTokens.CHARSET_KEYWORD) {
                builder.advanceLexer();

                expect(builder, CssTokens.STRING, CssLocalize.expectedCharsetName());

                expect(builder, CssTokens.SEMICOLON, CssLocalize.expectedSemicolon());

                marker.done(CssElements.CHARSET);
            }
            else if (builder.getTokenType() == CssTokens.IMPORT_KEYWORD) {
                builder.advanceLexer();

                if (!parseFunctionCall(builder)) {
                    expect(builder, CssTokens.STRING, CssLocalize.expectedImportUrl());
                }

                expect(builder, CssTokens.SEMICOLON, CssLocalize.expectedSemicolon());

                marker.done(CssElements.IMPORT);
            }
            else {
                if (!parseSelectorListNew(builder)) {
                    builder.error(CssLocalize.expectedSelector());
                }

                if (builder.getTokenType() == LBRACE) {
                    PsiBuilder.Marker bodyMarker = builder.mark();
                    builder.advanceLexer();

                    while (!builder.eof()) {
                        if (parseProperty(builder, null) == null) {
                            break;
                        }
                    }

                    expect(builder, RBRACE, CssLocalize.expectedRbrace());

                    bodyMarker.done(BLOCK);
                }
                else {
                    builder.error(CssLocalize.expectedLbrace());
                }

                marker.done(RULE);
            }
        }

        rootMarker.done(CssElements.ROOT);
    }

    @Nullable
    private PsiBuilder.Marker parseProperty(@Nonnull PsiBuilder builder, @Nullable PsiBuilder.Marker marker) {
        if (builder.getTokenType() == IDENTIFIER) {
            boolean isVariable = StringUtil.startsWith(Objects.requireNonNull(builder.getTokenSequence()), VAR_PREFIX);

            PsiBuilder.Marker propertyMarker = marker == null ? builder.mark() : marker;

            builder.advanceLexer();

            if (expect(builder, COLON, CssLocalize.expectedColon())) {
                parsePropertyValue(builder);
            }

            if (builder.getTokenType() == IMPORTANT_KEYWORD) {
                builder.advanceLexer();
            }

            boolean last = builder.getTokenType() == null || builder.getTokenType() == CssTokens.RBRACE;

            if (last) {
                optional(builder, SEMICOLON);
            }
            else {
                expect(builder, SEMICOLON, CssLocalize.expectedSemicolon());
            }

            propertyMarker.done(isVariable ? VARIABLE : PROPERTY);

            return propertyMarker;
        }
        else if (builder.getTokenType() == CssTokens.ASTERISK) {
            PsiBuilder.Marker propertyMarker = builder.mark();

            builder.advanceLexer();

            if (parseProperty(builder, propertyMarker) == null) {
                propertyMarker.error(CssLocalize.expectedName());
                return null;
            }
            return propertyMarker;
        }
        else {
            return null;
        }
    }

    public void parsePropertyValue(PsiBuilder builder) {
        PsiBuilder.Marker valueMarker = null;

        while (!builder.eof()) {
            IElementType type = builder.getTokenType();
            if (type == COMMA) {
                if (valueMarker != null) {
                    valueMarker.done(PROPERTY_VALUE_PART);
                    valueMarker = null;
                }

                builder.advanceLexer();
            }
            else if (type == SEMICOLON || type == RBRACE || type == IMPORTANT_KEYWORD) {
                break;
            }
            else {
                if (valueMarker == null) {
                    valueMarker = builder.mark();
                }

                if (parseFunctionCall(builder)) {
                    continue;
                }

                if (type == CssTokens.BAD_CHARACTER) {
                    PsiBuilder.Marker mark = builder.mark();
                    builder.advanceLexer();
                    mark.error(CssLocalize.badToken());
                }
                else {
                    builder.advanceLexer();
                }
            }
        }

        if (valueMarker != null) {
            valueMarker.done(PROPERTY_VALUE_PART);
        }
    }

    private boolean parseFunctionCall(PsiBuilder builder) {
        IElementType type = builder.getTokenType();
        if (type == CssTokens.IDENTIFIER && builder.lookAhead(1) == CssTokens.LPAR) {
            PsiBuilder.Marker functionMarker = builder.mark();

            CharSequence funcName = Objects.requireNonNull(builder.getTokenSequence());

            builder.advanceLexer();

            PsiBuilder.Marker argumentList = builder.mark();
            if (expect(builder, LPAR, CssLocalize.expectedLparen())) {
                if (StringUtil.equal(funcName, "var", false)) {
                    parseVarParameters(builder);
                }
                else {
                    parseSimpleParameters(builder);
                }

                expect(builder, RPAR, CssLocalize.expectedRparen());
            }
            argumentList.done(FUNCTION_CALL_PARAMETER_LIST);
            functionMarker.done(FUNCTION_CALL);
            return true;
        }
        return false;
    }

    private void parseSimpleParameters(PsiBuilder builder) {
        parseParameters(builder, (b, index) -> b.advanceLexer());
    }

    private void parseVarParameters(PsiBuilder builder) {
        parseParameters(builder, (b, index) ->
        {
            if (index == 0) {
                if (builder.getTokenType() == IDENTIFIER) {
                    boolean isVariable = StringUtil.startsWith(Objects.requireNonNull(builder.getTokenSequence()), VAR_PREFIX);

                    if (!isVariable) {
                        builder.error(CssLocalize.expectedVariable());
                        builder.advanceLexer();
                        return;
                    }

                    PsiBuilder.Marker mark = b.mark();
                    b.advanceLexer();
                    mark.done(VARIABLE_REFERENCE);
                }
                else {
                    builder.error(CssLocalize.expectedVariable());
                    builder.advanceLexer();
                }
            }
            else {
                b.advanceLexer();
            }
        });
    }

    private void parseParameters(PsiBuilder builder, BiConsumer<PsiBuilder, Integer> parameterParser) {
        int index = 0;
        boolean noArgument = true;
        while (!builder.eof()) {
            IElementType tokenType = builder.getTokenType();
            if (tokenType == COMMA) {
                if (noArgument) {
                    builder.error(CssLocalize.expectedArgument());
                    break;
                }

                builder.advanceLexer();
                noArgument = true;
                index++;
            }
            else if (tokenType == RPAR) {
                if (noArgument) {
                    builder.error(CssLocalize.expectedArgument());
                }
                break;
            }
            else {
                parameterParser.accept(builder, index);
                noArgument = false;
                index++;
            }
        }
    }

    public boolean parseSelectorListNew(PsiBuilder builder) {
        PsiBuilder.Marker listMarker = builder.mark();

        boolean errorSelector = false;

        PsiBuilder.Marker current = null;
        boolean first = true;
        boolean comma = false;
        while (!builder.eof()) {
            if (builder.getTokenType() == LBRACE) {
                if (first) {
                    errorSelector = true;
                }
                break;
            }

            if (!first) {
                comma = expect(builder, CssTokens.COMMA, CssLocalize.expectedComma());
            }

            PsiBuilder.Marker marker = parseSelector(builder);

            current = marker;

            if (marker == null) {
                if (first) {
                    errorSelector = true;
                }
            }

            if (marker == null) {
                if (builder.getTokenType() == CssTokens.LBRACE) {
                    break;
                }
                else {
                    markError(builder, CssLocalize.unexpectedToken());
                }
            }
            first = false;
        }
        if (comma && current == null) {
            builder.error(CssLocalize.expectedSelector());
        }
        listMarker.done(SELECTOR_LIST);
        return !errorSelector;
    }

    private void markError(PsiBuilder builder, LocalizeValue message) {
        PsiBuilder.Marker mark = builder.mark();
        builder.advanceLexer();
        mark.error(message);
    }

    @Nullable
    public PsiBuilder.Marker parseSelector(PsiBuilder builder) {
        PsiBuilder.Marker mark = builder.mark();
        if (!parseSimpleSelector(builder)) {
            mark.drop();
            return null;
        }

        while (parseSimpleSelector(builder)) {
        }

        while (builder.getTokenType() == CssTokens.PLUS
            || builder.getTokenType() == CssTokens.TILDE
            || builder.getTokenType() == CssTokens.GT) {
            builder.advanceLexer();

            if (parseSelector(builder) == null) {
                builder.error(CssLocalize.expectedSelector());
                break;
            }
        }

        mark.done(SELECTOR);
        return mark;
    }

    private boolean parseSimpleSelector(PsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();
        boolean isSelector;
        if ((isSelector = parseSelectorClassOrId(builder, false))
            || builder.getTokenType() == CssTokens.ASTERISK
            || builder.getTokenType() == CssTokens.IDENTIFIER) {
            if (isSelector) {
                parseSelectorClassOrId(builder, true);
            }
            else {
                builder.advanceLexer();
            }

            PsiBuilder.Marker suffixListMarker = builder.mark();

            parseSelectorAttributeList(builder);

            parseSelectorPseudoClass(builder);

            while (!builder.eof()) {
                if (parseSelectorClassOrId(builder, false)) {
                    PsiBuilder.Marker suffixMarker = builder.mark();

                    parseSelectorClassOrId(builder, true);

                    parseSelectorPseudoClass(builder);

                    suffixMarker.done(CssElements.SIMPLE_SELECTOR);
                }
                else {
                    break;
                }
            }
            suffixListMarker.done(CssElements.SELECTOR_SUFFIX_LIST);

            marker.done(CssElements.SIMPLE_SELECTOR);
            return true;
        }
        else if (builder.getTokenType() == LBRACKET) {
            parseSelectorAttributeList(builder);

            parseSelectorPseudoClass(builder);

            marker.done(CssElements.SIMPLE_SELECTOR);
            return true;
        }
        else if (builder.getTokenType() == CssTokens.COLON || builder.getTokenType() == CssTokens.COLONCOLON) {
            parseSelectorPseudoClass(builder);

            marker.done(CssElements.SIMPLE_SELECTOR);
            return true;
        }
        else {
            marker.drop();
            return false;
        }
    }

    private boolean parseSelectorClassOrId(PsiBuilder builder, boolean eat) {
        if (builder.getTokenType() == CssTokens.DOT && builder.lookAhead(1) == CssTokens.IDENTIFIER) {
            if (eat) {
                PsiBuilder.Marker mark = builder.mark();
                builder.advanceLexer();
                builder.advanceLexer();
                mark.collapse(CssTokens.SELECTOR_CLASS);
            }

            return true;
        }
        else if (builder.getTokenType() == CssTokens.SHARP && builder.lookAhead(1) == CssTokens.IDENTIFIER) {
            if (eat) {
                PsiBuilder.Marker mark = builder.mark();
                builder.advanceLexer();
                builder.advanceLexer();
                mark.collapse(CssTokens.SELECTOR_ID);
            }

            return true;
        }
        return false;
    }

    private void parseSelectorAttributeList(PsiBuilder builder) {
        if (builder.getTokenType() == LBRACKET) {
            PsiBuilder.Marker marker = builder.mark();

            builder.advanceLexer();

            while (parseSelectorAttribute(builder)) {
                if (builder.getTokenType() == COMMA) {
                    builder.advanceLexer();
                }
                else if (builder.getTokenType() == RBRACKET) {
                    break;
                }
            }

            expect(builder, RBRACKET, CssLocalize.expectedRbracket());

            marker.done(SELECTOR_ATTRIBUTE_LIST);
        }
    }

    private boolean parseSelectorAttribute(PsiBuilder builder) {
        if (builder.getTokenType() == IDENTIFIER) {
            PsiBuilder.Marker mark = builder.mark();

            builder.advanceLexer();

            if (CssTokenSets.SELECTOR_ATTRIBUTE_LIST_EQ.contains(builder.getTokenType())) {
                builder.advanceLexer();

                if (builder.getTokenType() == STRING || builder.getTokenType() == IDENTIFIER) {
                    builder.advanceLexer();
                }
                else {
                    builder.error(CssLocalize.expectedAttributeValue());
                }
            }
            mark.done(SELECTOR_ATTRIBUTE);
            return true;
        }
        else if (builder.getTokenType() == RBRACKET) {
        }
        else {
            builder.error(CssLocalize.expectedIdentifier());
        }
        return false;
    }

    private void parseSelectorPseudoClass(PsiBuilder builder) {
        if (builder.getTokenType() == COLON && builder.lookAhead(1) == IDENTIFIER && builder.lookAhead(2) == LPAR) {
            builder.advanceLexer();

            parseFunctionCall(builder);

            return;
        }

        while (builder.getTokenType() == COLON || builder.getTokenType() == COLONCOLON) {
            PsiBuilder.Marker marker = builder.mark();

            builder.advanceLexer();

            CharSequence name = builder.getTokenType() == IDENTIFIER ? builder.getTokenSequence() : null;

            if (expect(builder, IDENTIFIER, CssLocalize.expectedIdentifier()) && builder.getTokenType() == LPAR) {
                PsiBuilder.Marker argsMarker = builder.mark();

                builder.advanceLexer();

                if (StringUtil.equals(name, "not") && parseSelector(builder) == null) {
                    builder.error(CssLocalize.expectedSelector());
                }

                expect(builder, RPAR, CssLocalize.expectedRparen());

                argsMarker.done(SELECTOR_PSEUDO_CLASS_ARGUMENT_LIST);
            }

            marker.done(SELECTOR_PSEUDO_CLASS);
        }
    }
}
