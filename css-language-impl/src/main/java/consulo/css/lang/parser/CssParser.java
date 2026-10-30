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
import consulo.language.parser.WhitespacesBinders;
import consulo.language.version.LanguageVersion;
import consulo.localize.LocalizeValue;
import consulo.util.lang.StringUtil;

import org.jspecify.annotations.Nullable;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * @author VISTALL
 * @since 2013-07-03
 */
public class CssParser implements PsiParser, CssTokens, CssElements {
    public static final String VAR_PREFIX = "--";

    /**
     * Depth of style blocks currently being parsed. Inside a block (CSS Nesting) rules may be nested
     * and a selector may start with a combinator ({@code > .child}).
     */
    private int myNestingDepth;

    public static boolean expect(PsiBuilder builder, IElementType elementType, LocalizeValue message) {
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

    @Override
    public ASTNode parse(IElementType rootElementType, PsiBuilder builder, LanguageVersion languageVersion) {
        PsiBuilder.Marker mark = builder.mark();
        parseRoot(builder);
        mark.done(rootElementType);
        return builder.getTreeBuilt();
    }

    public void parseRoot(PsiBuilder builder) {
        PsiBuilder.Marker rootMarker = builder.mark();
        while (!builder.eof()) {
            if (!parseRootItem(builder)) {
                markError(builder, CssLocalize.unexpectedToken());
            }
        }

        rootMarker.done(CssElements.ROOT);
    }

    protected boolean parseRootItem(PsiBuilder builder) {
        if (builder.getTokenType() == CssTokens.CHARSET_KEYWORD) {
            PsiBuilder.Marker marker = builder.mark();
            builder.advanceLexer();

            expect(builder, CssTokens.STRING, CssLocalize.expectedCharsetName());

            expect(builder, CssTokens.SEMICOLON, CssLocalize.expectedSemicolon());

            marker.done(CssElements.CHARSET);
            return true;
        }
        else if (builder.getTokenType() == CssTokens.IMPORT_KEYWORD) {
            PsiBuilder.Marker marker = builder.mark();
            builder.advanceLexer();

            if (!parseFunctionCall(builder)) {
                expect(builder, CssTokens.STRING, CssLocalize.expectedImportUrl());
            }

            expect(builder, CssTokens.SEMICOLON, CssLocalize.expectedSemicolon());

            marker.done(CssElements.IMPORT);
            return true;
        }
        else if (builder.getTokenType() == CssTokens.MEDIA_KEYWORD) {
            parseMedia(builder);
            return true;
        }
        else if (builder.getTokenType() == CssTokens.FONT_FACE_KEYWORD) {
            parseFontFace(builder);
            return true;
        }
        else if (builder.getTokenType() == CssTokens.KEYFRAMES_KEYWORD) {
            parseKeyframes(builder);
            return true;
        }
        else if (isGenericAtRuleStart(builder.getTokenType())) {
            parseAtRule(builder);
            return true;
        }
        else {
            return parseRule(builder);
        }
    }

    protected void parseFontFace(PsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();
        builder.advanceLexer();

        parsePropertyBlock(builder);

        marker.done(FONT_FACE);
    }

    protected void parseKeyframes(PsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();
        builder.advanceLexer();

        expect(builder, IDENTIFIER, CssLocalize.expectedIdentifier());

        if (builder.getTokenType() == LBRACE) {
            PsiBuilder.Marker bodyMarker = builder.mark();
            builder.advanceLexer();

            while (!builder.eof() && builder.getTokenType() != RBRACE) {
                if (!parseKeyframeBlock(builder)) {
                    markError(builder, CssLocalize.unexpectedToken());
                }
            }

            expect(builder, RBRACE, CssLocalize.expectedRbrace());

            bodyMarker.done(BLOCK);
        }
        else {
            builder.error(CssLocalize.expectedLbrace());
        }

        marker.done(KEYFRAMES);
    }

    /**
     * Whether the token starts an at-rule. Dialects add their own at-rule keywords.
     */
    protected boolean isAtRuleStart(@Nullable IElementType type) {
        return type == MEDIA_KEYWORD
            || type == IMPORT_KEYWORD
            || type == CHARSET_KEYWORD
            || type == FONT_FACE_KEYWORD
            || type == KEYFRAMES_KEYWORD
            || isGenericAtRuleStart(type);
    }

    private static boolean isGenericAtRuleStart(@Nullable IElementType type) {
        return type == AT_KEYWORD || type == PAGE_KEYWORD || type == NAMESPACE_KEYWORD;
    }

    /**
     * At-rule without dedicated parsing: {@code @keyword prelude { block }} or {@code @keyword prelude;}.
     * The block may hold declarations ({@code @page}), rules ({@code @supports}, {@code @layer}) or both (CSS Nesting).
     */
    protected void parseAtRule(PsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();
        builder.advanceLexer();

        // prelude: up to the block or the end of the statement
        int parenDepth = 0;
        while (!builder.eof()) {
            IElementType type = builder.getTokenType();
            if (type == LPAR) {
                parenDepth++;
            }
            else if (type == RPAR) {
                parenDepth--;
            }
            else if (parenDepth <= 0 && (type == LBRACE || type == SEMICOLON || type == RBRACE)) {
                break;
            }
            builder.advanceLexer();
        }

        if (builder.getTokenType() == LBRACE) {
            parsePropertyBlock(builder);
        }
        else {
            optional(builder, SEMICOLON);
        }

        marker.done(AT_RULE);
    }

    /**
     * {@code @media} at the root, or nested inside a style rule (CSS Nesting), where its body may also hold declarations.
     */
    protected void parseMedia(PsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();
        builder.advanceLexer();

        parseMediaQueryList(builder);

        if (builder.getTokenType() == LBRACE) {
            PsiBuilder.Marker bodyMarker = builder.mark();
            builder.advanceLexer();

            boolean nested = myNestingDepth > 0 || isDeclarationAllowedInMediaBlock();
            while (!builder.eof() && builder.getTokenType() != RBRACE) {
                boolean parsed = nested ? parseBlockItem(builder) : parseRule(builder);
                if (!parsed) {
                    markError(builder, CssLocalize.unexpectedToken());
                }
            }

            expect(builder, RBRACE, CssLocalize.expectedRbrace());

            bodyMarker.done(BLOCK);
        }
        else {
            builder.error(CssLocalize.expectedLbrace());
        }

        marker.done(MEDIA);
    }

    /**
     * Whether the body of a top-level {@code @media} may hold declarations (PostCSS nesting), not only rules.
     */
    protected boolean isDeclarationAllowedInMediaBlock() {
        return false;
    }

    protected boolean parseRule(PsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();

        if (!parseSelectorListNew(builder)) {
            marker.drop();
            return false;
        }

        parsePropertyBlock(builder);

        marker.done(RULE);
        return true;
    }

    protected boolean parseKeyframeBlock(PsiBuilder builder) {
        if (!isKeyframeSelector(builder)) {
            return false;
        }

        PsiBuilder.Marker marker = builder.mark();

        parseKeyframeSelectors(builder);

        parsePropertyBlock(builder);

        marker.done(RULE);
        return true;
    }

    protected void parsePropertyBlock(PsiBuilder builder) {
        if (builder.getTokenType() == LBRACE) {
            PsiBuilder.Marker bodyMarker = builder.mark();
            builder.advanceLexer();

            myNestingDepth++;
            while (!builder.eof() && builder.getTokenType() != RBRACE) {
                if (!parseBlockItem(builder)) {
                    break;
                }
            }
            myNestingDepth--;

            expect(builder, RBRACE, CssLocalize.expectedRbrace());

            bodyMarker.done(getBlockElementType());
        }
        else {
            builder.error(CssLocalize.expectedLbrace());
        }
    }

    protected IElementType getBlockElementType() {
        return BLOCK;
    }

    /**
     * One item of a style block: a declaration, a nested rule or a nested {@code @media} (CSS Nesting).
     *
     * @return false if nothing could be parsed
     */
    protected boolean parseBlockItem(PsiBuilder builder) {
        if (builder.getTokenType() == MEDIA_KEYWORD) {
            parseMedia(builder);
            return true;
        }

        if (isGenericAtRuleStart(builder.getTokenType())) {
            parseAtRule(builder);
            return true;
        }

        // inside at-rule blocks: "@starting-style { @keyframes x {} }", "@layer x { @font-face {} }"
        if (builder.getTokenType() == KEYFRAMES_KEYWORD) {
            parseKeyframes(builder);
            return true;
        }

        if (builder.getTokenType() == FONT_FACE_KEYWORD) {
            parseFontFace(builder);
            return true;
        }

        if (isNestedRuleStart(builder)) {
            return parseRule(builder);
        }

        return parseProperty(builder, null) != null;
    }

    protected boolean isNestedRuleStart(PsiBuilder builder) {
        IElementType type = builder.getTokenType();
        if (type == IDENTIFIER) {
            if (StringUtil.startsWith(Objects.requireNonNull(builder.getTokenSequence()), VAR_PREFIX)) {
                // custom property: its value may legally contain braces
                return false;
            }
            return isFollowedByBlock(builder);
        }
        else if (type == ASTERISK) {
            // "* { }" universal selector vs "*zoom: 1;" property hack
            return isFollowedByBlock(builder);
        }
        return type == AMPERSAND
            || type == DOT
            || type == SHARP
            || type == LBRACKET
            || type == COLON
            || type == COLONCOLON
            || type == PIPE
            || isCombinator(type);
    }

    /**
     * Whether "{" comes before the end of a declaration (";" or "}"), which makes the current item a nested rule:
     * {@code a:hover { }} is a rule, {@code color: red;} is a declaration.
     */
    private static boolean isFollowedByBlock(PsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();
        try {
            int parenDepth = 0;
            while (!builder.eof()) {
                IElementType type = builder.getTokenType();
                if (type == LPAR) {
                    parenDepth++;
                }
                else if (type == RPAR) {
                    parenDepth--;
                }
                else if (parenDepth <= 0 && (type == SEMICOLON || type == RBRACE)) {
                    return false;
                }
                else if (parenDepth <= 0 && type == LBRACE) {
                    return true;
                }
                builder.advanceLexer();
            }
            return false;
        }
        finally {
            marker.rollbackTo();
        }
    }

    protected static boolean isCombinator(@Nullable IElementType type) {
        return type == GT || type == PLUS || type == TILDE || type == GT_GT_GT;
    }

    protected boolean isKeyframeSelector(PsiBuilder builder) {
        IElementType type = builder.getTokenType();
        if (type == IDENTIFIER) {
            CharSequence text = builder.getTokenSequence();
            return StringUtil.equals(text, "from") || StringUtil.equals(text, "to");
        }
        return type == NUMBER;
    }

    protected void parseKeyframeSelectors(PsiBuilder builder) {
        PsiBuilder.Marker selectorMarker = builder.mark();

        parseKeyframeSelectorValue(builder);

        while (builder.getTokenType() == COMMA) {
            builder.advanceLexer();
            parseKeyframeSelectorValue(builder);
        }

        selectorMarker.done(KEYFRAME_SELECTOR);
    }

    protected void parseKeyframeSelectorValue(PsiBuilder builder) {
        if (builder.getTokenType() == IDENTIFIER) {
            CharSequence text = builder.getTokenSequence();
            if (StringUtil.equals(text, "from") || StringUtil.equals(text, "to")) {
                builder.advanceLexer();
            }
            else {
                builder.error(CssLocalize.expectedIdentifier());
            }
        }
        else if (builder.getTokenType() == NUMBER) {
            builder.advanceLexer();
            // handle case where % is separate token after number
            optional(builder, PERC);
        }
        else {
            builder.error(CssLocalize.expectedIdentifier());
        }
    }

    protected void parseMediaQueryList(PsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();
        int start = builder.getCurrentOffset();

        while (!builder.eof()) {
            IElementType type = builder.getTokenType();
            // a missing ";" / "{": the next statement starts
            if (type == LBRACE || type == SEMICOLON || type == RBRACE || isAtRuleStart(type)) {
                break;
            }
            else if (type == LPAR) {
                builder.advanceLexer();

                while (!builder.eof()
                    && builder.getTokenType() != RPAR
                    && builder.getTokenType() != SEMICOLON
                    && builder.getTokenType() != RBRACE) {
                    builder.advanceLexer();
                }

                optional(builder, RPAR);
            }
            else {
                builder.advanceLexer();
            }
        }

        if (builder.getCurrentOffset() == start) {
            // an empty list stays in front of the following whitespace, where a missing part is reported
            marker.setCustomEdgeTokenBinders(WhitespacesBinders.GREEDY_LEFT_BINDER, null);
        }
        marker.done(MEDIA_QUERY_LIST);
    }

    protected PsiBuilder.@Nullable Marker parseProperty(PsiBuilder builder, PsiBuilder.@Nullable Marker marker) {
        if (builder.getTokenType() == IDENTIFIER) {
            boolean isVariable = StringUtil.startsWith(Objects.requireNonNull(builder.getTokenSequence()), VAR_PREFIX);

            PsiBuilder.Marker propertyMarker = marker == null ? builder.mark() : marker;

            parsePropertyName(builder);

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

    /**
     * Name of a declaration, current token is an identifier. Dialects may glue more tokens to it ({@code margin-$dir}).
     */
    protected void parsePropertyName(PsiBuilder builder) {
        builder.advanceLexer();
    }

    /**
     * Dialect-specific part of a property value ({@code $var}, ...).
     *
     * @return true if a part was parsed
     */
    protected boolean parseDialectPropertyValuePart(PsiBuilder builder) {
        return false;
    }

    /**
     * Dialect-specific end of a property value, e.g. the start of the next declaration when a semicolon is missing.
     */
    protected boolean isDialectPropertyValueEnd(PsiBuilder builder) {
        return false;
    }

    public void parsePropertyValue(PsiBuilder builder) {
        while (!builder.eof()) {
            IElementType type = builder.getTokenType();
            if (type == COMMA || type == SLASH) {
                // Comma and slash are value separators in CSS shorthand properties
                // e.g., "font: 12px/1.5 sans-serif", "grid-area: 1 / 1"
                builder.advanceLexer();
            }
            else if (type == SEMICOLON || type == RBRACE || type == IMPORTANT_KEYWORD) {
                break;
            }
            else if (isDialectPropertyValueEnd(builder)) {
                break;
            }
            else if (parseDialectPropertyValuePart(builder)) {
                continue;
            }
            else {
                // Each space-separated token (or function call) becomes its own PROPERTY_VALUE_PART.
                // This allows proper validation of multi-value shorthand properties like
                // "border: 1px solid rgba(255, 255, 255, 0.8)"
                PsiBuilder.Marker valueMarker = builder.mark();

                if (parseFunctionCall(builder)) {
                    valueMarker.done(PROPERTY_VALUE_PART);
                    continue;
                }

                if (type == CssTokens.SHARP) {
                    // Hex color: # followed by hex digits (e.g., #272c32)
                    // The lexer may split hex digits into multiple tokens:
                    //   #272c32 → SHARP + NUMBER(272) + IDENTIFIER(c32)
                    //   #fff    → SHARP + IDENTIFIER(fff)
                    // Count adjacent (no whitespace gap) NUMBER/IDENTIFIER tokens via rawLookup
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
                        // Collapse SHARP + all adjacent hex digit tokens into a single NUMBER token
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
                    // The lexer may split number+unit into two tokens for modern units
                    // not in the lexer's suffix list (e.g., rem, vw, vh, vmin, vmax, ch, etc.):
                    //   1.3rem → NUMBER(1.3) + IDENTIFIER(rem)
                    // Collapse adjacent NUMBER + IDENTIFIER into a single NUMBER token
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

                valueMarker.done(PROPERTY_VALUE_PART);
            }
        }
    }

    protected boolean parseFunctionCall(PsiBuilder builder) {
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

    protected void parseSimpleParameters(PsiBuilder builder) {
        parseParameters(builder, (b, index) -> parseParameterPart(b));
    }

    /**
     * Dialect-specific part of a function argument ({@code $var} in {@code calc(4 * $var)}).
     *
     * @return true if a part was parsed
     */
    protected boolean parseDialectParameterPart(PsiBuilder builder) {
        return false;
    }

    /**
     * One part of a function argument: a nested call ({@code style(--x: y)}), a parenthesized group or a token.
     */
    protected void parseParameterPart(PsiBuilder builder) {
        if (parseDialectParameterPart(builder)) {
            return;
        }

        if (parseFunctionCall(builder)) {
            return;
        }

        if (builder.getTokenType() == LPAR) {
            int depth = 0;
            do {
                IElementType type = builder.getTokenType();
                if (type == LPAR) {
                    depth++;
                }
                else if (type == RPAR) {
                    depth--;
                }
                builder.advanceLexer();
            }
            while (!builder.eof() && depth > 0);
            return;
        }

        builder.advanceLexer();
    }

    protected void parseVarParameters(PsiBuilder builder) {
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

    protected void parseParameters(PsiBuilder builder, BiConsumer<PsiBuilder, Integer> parameterParser) {
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
                // "f()" has no arguments, "f(a,)" misses one
                if (noArgument && index > 0) {
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
            IElementType tokenType = builder.getTokenType();
            if (tokenType == LBRACE || tokenType == SEMICOLON || tokenType == RBRACE) {
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

    protected void markError(PsiBuilder builder, LocalizeValue message) {
        PsiBuilder.Marker mark = builder.mark();
        builder.advanceLexer();
        mark.error(message);
    }

    public PsiBuilder.@Nullable Marker parseSelector(PsiBuilder builder) {
        PsiBuilder.Marker mark = builder.mark();

        // relative selector inside a style block (CSS Nesting): "> .child", "+ .sibling", "~ .sibling"
        boolean relative = myNestingDepth > 0 && isCombinator(builder.getTokenType());
        if (relative) {
            builder.advanceLexer();
        }

        if (!parseSimpleSelector(builder)) {
            if (relative) {
                builder.error(CssLocalize.expectedSelector());
                mark.done(SELECTOR);
                return mark;
            }
            mark.drop();
            return null;
        }

        while (parseSimpleSelector(builder)) {
        }

        while (isCombinator(builder.getTokenType())) {
            builder.advanceLexer();

            if (parseSelector(builder) == null) {
                builder.error(CssLocalize.expectedSelector());
                break;
            }
        }

        mark.done(SELECTOR);
        return mark;
    }

    protected boolean parseSimpleSelector(PsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();
        boolean isSelector;
        if ((isSelector = parseSelectorClassOrId(builder, false))
            || builder.getTokenType() == CssTokens.ASTERISK
            || builder.getTokenType() == CssTokens.IDENTIFIER
            // nesting selector (CSS Nesting): "&", "&.active", "&:hover"
            || builder.getTokenType() == CssTokens.AMPERSAND
            // no namespace: "|a"
            || builder.getTokenType() == CssTokens.PIPE && isNamespacedName(builder.lookAhead(1))) {
            if (isSelector) {
                parseSelectorClassOrId(builder, true);
            }
            else {
                boolean namespaceStart = builder.getTokenType() == CssTokens.PIPE;
                builder.advanceLexer();
                if (namespaceStart) {
                    builder.advanceLexer();
                }
                // namespace prefix: "svg|path", "*|a"
                else if (builder.getTokenType() == CssTokens.PIPE && !isWhitespaceBefore(builder) && isNamespacedName(builder.rawLookup(1))) {
                    builder.advanceLexer();
                    builder.advanceLexer();
                }
            }

            parseSelectorSuffixList(builder);

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

    private static boolean isNamespacedName(@Nullable IElementType type) {
        return type == CssTokens.IDENTIFIER || type == CssTokens.ASTERISK;
    }

    /**
     * Attribute selectors, pseudo-classes, classes and ids attached to the start of a compound selector.
     */
    protected void parseSelectorSuffixList(PsiBuilder builder) {
        PsiBuilder.Marker suffixListMarker = builder.mark();

        // whitespace is a descendant combinator: "& .title" and "div .title" are two compound selectors
        if (!isWhitespaceBefore(builder)) {
            parseSelectorAttributeList(builder);

            parseSelectorPseudoClass(builder);
        }

        while (!builder.eof() && !isWhitespaceBefore(builder)) {
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
        // an empty suffix list stays in front of the following whitespace, so the selector doesn't end with it
        suffixListMarker.setCustomEdgeTokenBinders(WhitespacesBinders.GREEDY_LEFT_BINDER, null);
        suffixListMarker.done(CssElements.SELECTOR_SUFFIX_LIST);
    }

    /**
     * Whether whitespace or a comment separates the current token from the previous one.
     */
    protected static boolean isWhitespaceBefore(PsiBuilder builder) {
        IElementType previous = builder.rawLookup(-1);
        return previous == WHITE_SPACE || CssTokenSets.COMMENTS.contains(previous);
    }

    protected boolean parseSelectorClassOrId(PsiBuilder builder, boolean eat) {
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

    protected void parseSelectorAttributeList(PsiBuilder builder) {
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

    protected boolean parseSelectorAttribute(PsiBuilder builder) {
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

    protected void parseSelectorPseudoClass(PsiBuilder builder) {
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

                if (StringUtil.equals(name, "not")) {
                    if (parseSelector(builder) == null) {
                        builder.error(CssLocalize.expectedSelector());
                    }
                }
                else {
                    // Consume arguments for pseudo-classes/pseudo-elements like
                    // ::part(drawer), :nth-child(2n+1), :has(.class), :where(a, b)
                    int depth = 1;
                    while (!builder.eof() && depth > 0) {
                        IElementType tokenType = builder.getTokenType();
                        if (tokenType == LPAR) {
                            depth++;
                        }
                        else if (tokenType == RPAR) {
                            depth--;
                            if (depth == 0) {
                                break;
                            }
                        }
                        builder.advanceLexer();
                    }
                }

                expect(builder, RPAR, CssLocalize.expectedRparen());

                argsMarker.done(SELECTOR_PSEUDO_CLASS_ARGUMENT_LIST);
            }

            marker.done(SELECTOR_PSEUDO_CLASS);
        }
    }
}
