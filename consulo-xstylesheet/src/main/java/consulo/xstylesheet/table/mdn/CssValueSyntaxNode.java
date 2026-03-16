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

import java.util.List;

/**
 * AST node representing a parsed CSS Value Definition Syntax expression.
 * <p>
 * The CSS Value Definition Syntax is used in CSS specifications to define
 * the set of valid values for a CSS property. This AST represents the parsed
 * form of such syntax strings.
 *
 * @author VISTALL
 * @since 2026-03-15
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/CSS/Value_definition_syntax">CSS Value Definition Syntax</a>
 */
public sealed interface CssValueSyntaxNode {

    /**
     * A literal keyword like "auto", "none", "inherit", "initial", "unset".
     */
    record Keyword(String value) implements CssValueSyntaxNode {
        @Override
        public String toString() {
            return value;
        }
    }

    /**
     * A data type reference like {@code <length>}, {@code <color>}, {@code <percentage>}.
     */
    record DataType(String name) implements CssValueSyntaxNode {
        @Override
        public String toString() {
            return "<" + name + ">";
        }
    }

    /**
     * A property reference like {@code <'flex-grow'>}, which references another property's syntax.
     */
    record PropertyRef(String propertyName) implements CssValueSyntaxNode {
        @Override
        public String toString() {
            return "<'" + propertyName + "'>";
        }
    }

    /**
     * A function call syntax like {@code fit-content( <length-percentage> )}.
     */
    record Function(String name, CssValueSyntaxNode arguments) implements CssValueSyntaxNode {
        @Override
        public String toString() {
            return name + "(" + (arguments != null ? arguments : "") + ")";
        }
    }

    /**
     * A group of alternatives or combinations.
     */
    record Group(CombinatorType combinatorType, List<CssValueSyntaxNode> children) implements CssValueSyntaxNode {
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            String sep = switch (combinatorType) {
                case ALTERNATIVES -> " | ";
                case ANY_ORDER_AT_LEAST_ONE -> " || ";
                case ALL_ANY_ORDER -> " && ";
                case JUXTAPOSITION -> " ";
            };
            for (int i = 0; i < children.size(); i++) {
                if (i > 0) {
                    sb.append(sep);
                }
                sb.append(children.get(i));
            }
            return sb.toString();
        }
    }

    /**
     * A multiplier applied to a node (?, *, +, {n,m}, #).
     */
    record Multiplied(CssValueSyntaxNode child, MultiplierType multiplierType, int min,
                      int max) implements CssValueSyntaxNode {
        @Override
        public String toString() {
            String suffix = switch (multiplierType) {
                case OPTIONAL -> "?";
                case ZERO_OR_MORE -> "*";
                case ONE_OR_MORE -> "+";
                case RANGE -> "{" + min + "," + max + "}";
                case COMMA_SEPARATED -> "#";
                case REQUIRED -> "!";
            };
            return child.toString() + suffix;
        }
    }

    /**
     * Combinator types in CSS Value Definition Syntax, listed from lowest to highest precedence.
     */
    enum CombinatorType {
        /**
         * {@code a | b} — exactly one of the alternatives
         */
        ALTERNATIVES,
        /**
         * {@code a || b} — one or more, in any order
         */
        ANY_ORDER_AT_LEAST_ONE,
        /**
         * {@code a && b} — all required, in any order
         */
        ALL_ANY_ORDER,
        /**
         * {@code a b} — all in the given order (juxtaposition)
         */
        JUXTAPOSITION
    }

    /**
     * Multiplier types.
     */
    enum MultiplierType {
        OPTIONAL,           // ?
        ZERO_OR_MORE,       // *
        ONE_OR_MORE,        // +
        RANGE,              // {n,m}
        COMMA_SEPARATED,    // #
        REQUIRED            // !
    }
}
