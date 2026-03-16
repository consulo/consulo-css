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

package consulo.xstylesheet.table.mdn;

import consulo.xstylesheet.table.mdn.CssValueSyntaxNode.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Parser for the CSS Value Definition Syntax used in CSS specifications.
 * <p>
 * Parses syntax strings like {@code "auto | <length-percentage> | min-content"}
 * into an AST of {@link CssValueSyntaxNode}.
 * <p>
 * Operator precedence (lowest to highest):
 * <ol>
 *   <li>{@code |} — alternatives (single bar)</li>
 *   <li>{@code ||} — any order, at least one (double bar)</li>
 *   <li>{@code &&} — all required, any order (double ampersand)</li>
 *   <li>juxtaposition (space) — all in order</li>
 * </ol>
 *
 * @author VISTALL
 * @since 2026-03-15
 */
public class CssValueSyntaxParser {
    private String input;
    private int pos;

    public CssValueSyntaxNode parse(String syntax) {
        this.input = syntax.trim();
        this.pos = 0;

        if (input.isEmpty()) {
            return new Keyword("");
        }

        CssValueSyntaxNode result = parseAlternatives();
        return result;
    }

    // Level 1: a | b | c (lowest precedence)
    private CssValueSyntaxNode parseAlternatives() {
        CssValueSyntaxNode left = parseAnyOrder();
        List<CssValueSyntaxNode> nodes = null;

        while (true) {
            skipWhitespace();
            if (pos < input.length() && input.charAt(pos) == '|') {
                // check it's single | not ||
                if (pos + 1 < input.length() && input.charAt(pos + 1) == '|') {
                    break; // double bar, handled at next level
                }
                pos++; // consume |
                skipWhitespace();
                if (nodes == null) {
                    nodes = new ArrayList<>();
                    nodes.add(left);
                }
                nodes.add(parseAnyOrder());
            }
            else {
                break;
            }
        }

        if (nodes != null) {
            return new Group(CombinatorType.ALTERNATIVES, nodes);
        }
        return left;
    }

    // Level 2: a || b || c
    private CssValueSyntaxNode parseAnyOrder() {
        CssValueSyntaxNode left = parseAllAnyOrder();
        List<CssValueSyntaxNode> nodes = null;

        while (true) {
            skipWhitespace();
            if (pos + 1 < input.length() && input.charAt(pos) == '|' && input.charAt(pos + 1) == '|') {
                pos += 2; // consume ||
                skipWhitespace();
                if (nodes == null) {
                    nodes = new ArrayList<>();
                    nodes.add(left);
                }
                nodes.add(parseAllAnyOrder());
            }
            else {
                break;
            }
        }

        if (nodes != null) {
            return new Group(CombinatorType.ANY_ORDER_AT_LEAST_ONE, nodes);
        }
        return left;
    }

    // Level 3: a && b && c
    private CssValueSyntaxNode parseAllAnyOrder() {
        CssValueSyntaxNode left = parseJuxtaposition();
        List<CssValueSyntaxNode> nodes = null;

        while (true) {
            skipWhitespace();
            if (pos + 1 < input.length() && input.charAt(pos) == '&' && input.charAt(pos + 1) == '&') {
                pos += 2; // consume &&
                skipWhitespace();
                if (nodes == null) {
                    nodes = new ArrayList<>();
                    nodes.add(left);
                }
                nodes.add(parseJuxtaposition());
            }
            else {
                break;
            }
        }

        if (nodes != null) {
            return new Group(CombinatorType.ALL_ANY_ORDER, nodes);
        }
        return left;
    }

    // Level 4: a b c (juxtaposition — highest combinator precedence)
    private CssValueSyntaxNode parseJuxtaposition() {
        CssValueSyntaxNode first = parseMultiplied();
        List<CssValueSyntaxNode> nodes = null;

        while (true) {
            skipWhitespace();
            if (pos >= input.length()) {
                break;
            }

            char c = input.charAt(pos);
            // Stop at combinators of lower precedence or group closing
            if (c == '|' || c == '&' || c == ']' || c == ')') {
                break;
            }

            if (nodes == null) {
                nodes = new ArrayList<>();
                nodes.add(first);
            }
            nodes.add(parseMultiplied());
        }

        if (nodes != null) {
            return new Group(CombinatorType.JUXTAPOSITION, nodes);
        }
        return first;
    }

    // Parse a primary with optional multiplier: atom?, atom*, atom+, atom{n,m}, atom#
    private CssValueSyntaxNode parseMultiplied() {
        CssValueSyntaxNode node = parsePrimary();

        skipWhitespace();
        if (pos >= input.length()) {
            return node;
        }

        char c = input.charAt(pos);
        if (c == '?') {
            pos++;
            return new Multiplied(node, MultiplierType.OPTIONAL, 0, 1);
        }
        else if (c == '*') {
            pos++;
            return new Multiplied(node, MultiplierType.ZERO_OR_MORE, 0, Integer.MAX_VALUE);
        }
        else if (c == '+') {
            pos++;
            return new Multiplied(node, MultiplierType.ONE_OR_MORE, 1, Integer.MAX_VALUE);
        }
        else if (c == '#') {
            pos++;
            // # can be followed by {n,m} for comma-separated with range
            if (pos < input.length() && input.charAt(pos) == '{') {
                int[] range = parseRange();
                return new Multiplied(node, MultiplierType.COMMA_SEPARATED, range[0], range[1]);
            }
            return new Multiplied(node, MultiplierType.COMMA_SEPARATED, 1, Integer.MAX_VALUE);
        }
        else if (c == '!') {
            pos++;
            return new Multiplied(node, MultiplierType.REQUIRED, 1, 1);
        }
        else if (c == '{') {
            int[] range = parseRange();
            return new Multiplied(node, MultiplierType.RANGE, range[0], range[1]);
        }

        return node;
    }

    // Parse {n} or {n,m}
    private int[] parseRange() {
        pos++; // consume {
        skipWhitespace();
        int min = parseInteger();
        int max = min;
        skipWhitespace();
        if (pos < input.length() && input.charAt(pos) == ',') {
            pos++; // consume ,
            skipWhitespace();
            if (pos < input.length() && input.charAt(pos) != '}') {
                max = parseInteger();
            }
            else {
                max = Integer.MAX_VALUE;
            }
        }
        skipWhitespace();
        if (pos < input.length() && input.charAt(pos) == '}') {
            pos++; // consume }
        }
        return new int[]{min, max};
    }

    private int parseInteger() {
        int start = pos;
        while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
            pos++;
        }
        if (start == pos) {
            return 0;
        }
        return Integer.parseInt(input.substring(start, pos));
    }

    // Parse primary: keyword, <data-type>, <'property-ref'>, [...], function(...)
    private CssValueSyntaxNode parsePrimary() {
        skipWhitespace();
        if (pos >= input.length()) {
            return new Keyword("");
        }

        char c = input.charAt(pos);

        // Comma literal (used in CSS syntax like "[ <bg-layer> , ]* <final-bg-layer>")
        if (c == ',') {
            pos++;
            return new Keyword(",");
        }

        // Slash literal (used in CSS syntax like "<font-size> / <line-height>")
        if (c == '/') {
            pos++;
            return new Keyword("/");
        }

        // Bracketed group: [ ... ]
        if (c == '[') {
            pos++; // consume [
            skipWhitespace();
            CssValueSyntaxNode inner = parseAlternatives();
            skipWhitespace();
            if (pos < input.length() && input.charAt(pos) == ']') {
                pos++; // consume ]
            }
            return inner;
        }

        // Data type reference: <...> or <'...'>
        if (c == '<') {
            return parseDataType();
        }

        // Keyword or function name
        return parseKeywordOrFunction();
    }

    // Parse <data-type> or <'property-ref'> or <data-type [constraints]>
    private CssValueSyntaxNode parseDataType() {
        pos++; // consume <
        skipWhitespace();

        if (pos < input.length() && input.charAt(pos) == '\'') {
            // Property reference: <'property-name'>
            pos++; // consume opening '
            int start = pos;
            while (pos < input.length() && input.charAt(pos) != '\'') {
                pos++;
            }
            String propName = input.substring(start, pos);
            if (pos < input.length()) {
                pos++; // consume closing '
            }
            // skip to >
            while (pos < input.length() && input.charAt(pos) != '>') {
                pos++;
            }
            if (pos < input.length()) {
                pos++; // consume >
            }
            return new PropertyRef(propName);
        }

        // Regular data type: <type-name> possibly with constraints like [0,∞]
        int start = pos;
        int angleBracketDepth = 1;
        while (pos < input.length() && angleBracketDepth > 0) {
            char ch = input.charAt(pos);
            if (ch == '<') {
                angleBracketDepth++;
            }
            else if (ch == '>') {
                angleBracketDepth--;
            }
            if (angleBracketDepth > 0) {
                pos++;
            }
        }
        String typeName = input.substring(start, pos).trim();
        if (pos < input.length()) {
            pos++; // consume >
        }

        // Strip constraints like " [0,∞]"
        int bracketIdx = typeName.indexOf('[');
        if (bracketIdx > 0) {
            typeName = typeName.substring(0, bracketIdx).trim();
        }

        // Check for function syntax: e.g., type-name(...)
        skipWhitespace();
        if (pos < input.length() && input.charAt(pos) == '(') {
            pos++; // consume (
            skipWhitespace();
            CssValueSyntaxNode args = null;
            if (pos < input.length() && input.charAt(pos) != ')') {
                args = parseAlternatives();
            }
            skipWhitespace();
            if (pos < input.length() && input.charAt(pos) == ')') {
                pos++; // consume )
            }
            return new Function(typeName, args);
        }

        return new DataType(typeName);
    }

    // Parse a keyword (identifier) or function call like "fit-content(...)"
    private CssValueSyntaxNode parseKeywordOrFunction() {
        int start = pos;

        // Read identifier: letters, digits, hyphens
        while (pos < input.length()) {
            char ch = input.charAt(pos);
            if (Character.isLetterOrDigit(ch) || ch == '-' || ch == '_') {
                pos++;
            }
            else {
                break;
            }
        }

        if (start == pos) {
            // Unexpected character, skip it
            pos++;
            return new Keyword(String.valueOf(input.charAt(start)));
        }

        String word = input.substring(start, pos);

        // Check for function call: keyword(...)
        if (pos < input.length() && input.charAt(pos) == '(') {
            pos++; // consume (
            skipWhitespace();
            CssValueSyntaxNode args = null;
            if (pos < input.length() && input.charAt(pos) != ')') {
                args = parseAlternatives();
            }
            skipWhitespace();
            if (pos < input.length() && input.charAt(pos) == ')') {
                pos++; // consume )
            }
            return new Function(word, args);
        }

        return new Keyword(word);
    }

    private void skipWhitespace() {
        while (pos < input.length() && Character.isWhitespace(input.charAt(pos))) {
            pos++;
        }
    }
}
