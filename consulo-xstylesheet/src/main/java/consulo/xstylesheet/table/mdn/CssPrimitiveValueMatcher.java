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

import java.util.Set;
import java.util.regex.Pattern;

/**
 * Matches CSS value tokens against CSS primitive data types
 * like {@code <length>}, {@code <color>}, {@code <number>}, etc.
 *
 * @author VISTALL
 * @since 2026-03-15
 */
public class CssPrimitiveValueMatcher {
    private static final Pattern HEX_COLOR_PATTERN = Pattern.compile("#[0-9a-fA-F]{3,8}");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("[+-]?\\d*\\.?\\d+([eE][+-]?\\d+)?");
    private static final Pattern INTEGER_PATTERN = Pattern.compile("[+-]?\\d+");

    private static final Set<String> LENGTH_UNITS = Set.of(
        "px", "em", "rem", "ex", "ch",
        "vw", "vh", "vmin", "vmax",
        "svw", "svh", "lvw", "lvh", "dvw", "dvh",
        "cm", "mm", "in", "pt", "pc", "Q"
    );

    private static final Set<String> ANGLE_UNITS = Set.of("deg", "rad", "grad", "turn");

    private static final Set<String> TIME_UNITS = Set.of("s", "ms");

    private static final Set<String> RESOLUTION_UNITS = Set.of("dpi", "dpcm", "dppx", "x");

    private static final Set<String> FREQUENCY_UNITS = Set.of("Hz", "kHz");

    private static final Set<String> NAMED_COLORS = Set.of(
        "aliceblue", "antiquewhite", "aqua", "aquamarine", "azure",
        "beige", "bisque", "black", "blanchedalmond", "blue", "blueviolet", "brown", "burlywood",
        "cadetblue", "chartreuse", "chocolate", "coral", "cornflowerblue", "cornsilk", "crimson", "cyan",
        "darkblue", "darkcyan", "darkgoldenrod", "darkgray", "darkgreen", "darkgrey", "darkkhaki",
        "darkmagenta", "darkolivegreen", "darkorange", "darkorchid", "darkred", "darksalmon",
        "darkseagreen", "darkslateblue", "darkslategray", "darkslategrey", "darkturquoise", "darkviolet",
        "deeppink", "deepskyblue", "dimgray", "dimgrey", "dodgerblue",
        "firebrick", "floralwhite", "forestgreen", "fuchsia",
        "gainsboro", "ghostwhite", "gold", "goldenrod", "gray", "green", "greenyellow", "grey",
        "honeydew", "hotpink",
        "indianred", "indigo", "ivory",
        "khaki",
        "lavender", "lavenderblush", "lawngreen", "lemonchiffon", "lightblue", "lightcoral",
        "lightcyan", "lightgoldenrodyellow", "lightgray", "lightgreen", "lightgrey", "lightpink",
        "lightsalmon", "lightseagreen", "lightskyblue", "lightslategray", "lightslategrey",
        "lightsteelblue", "lightyellow", "lime", "limegreen", "linen",
        "magenta", "maroon", "mediumaquamarine", "mediumblue", "mediumorchid", "mediumpurple",
        "mediumseagreen", "mediumslateblue", "mediumspringgreen", "mediumturquoise", "mediumvioletred",
        "midnightblue", "mintcream", "mistyrose", "moccasin",
        "navajowhite", "navy",
        "oldlace", "olive", "olivedrab", "orange", "orangered", "orchid",
        "palegoldenrod", "palegreen", "paleturquoise", "palevioletred", "papayawhip", "peachpuff",
        "peru", "pink", "plum", "powderblue", "purple",
        "rebeccapurple", "red", "rosybrown", "royalblue",
        "saddlebrown", "salmon", "sandybrown", "seagreen", "seashell", "sienna", "silver",
        "skyblue", "slateblue", "slategray", "slategrey", "snow", "springgreen", "steelblue",
        "tan", "teal", "thistle", "tomato", "turquoise",
        "violet",
        "wheat", "white", "whitesmoke",
        "yellow", "yellowgreen",
        "transparent", "currentcolor", "currentColor"
    );

    private static final Set<String> GLOBAL_VALUES = Set.of(
        "inherit", "initial", "unset", "revert", "revert-layer"
    );

    private static final Set<String> ABSOLUTE_SIZE_KEYWORDS = Set.of(
        "xx-small", "x-small", "small", "medium", "large", "x-large", "xx-large", "xxx-large"
    );

    private static final Set<String> RELATIVE_SIZE_KEYWORDS = Set.of(
        "smaller", "larger"
    );

    /**
     * Check if a CSS value token matches a given primitive data type.
     *
     * @param dataTypeName the data type name (without angle brackets), e.g., "length", "color"
     * @param value        the CSS value token to check
     * @return true if the value matches the data type
     */
    public static boolean matchesDataType(String dataTypeName, String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }

        String trimmed = value.trim();
        String lowerValue = trimmed.toLowerCase();

        return switch (dataTypeName) {
            case "number" -> isNumber(trimmed);
            case "integer" -> isInteger(trimmed);
            case "length" -> isLength(trimmed);
            case "percentage" -> isPercentage(trimmed);
            case "length-percentage" -> isLength(trimmed) || isPercentage(trimmed);
            case "color" -> isColor(lowerValue);
            case "string" -> isString(trimmed);
            case "url" -> isFunctionCall(trimmed, "url");
            case "image" -> isImage(lowerValue);
            case "angle" -> isAngle(trimmed);
            case "time" -> isTime(trimmed);
            case "resolution" -> isResolution(trimmed);
            case "frequency" -> isFrequency(trimmed);
            case "flex" -> isNumberWithUnit(trimmed, Set.of("fr"));
            case "custom-ident" -> isCustomIdent(trimmed);
            case "ident" -> isIdent(trimmed);
            case "dashed-ident" -> trimmed.startsWith("--") && isIdent(trimmed.substring(2));
            case "alpha-value" -> isNumber(trimmed) || isPercentage(trimmed);
            case "position" -> isPosition(lowerValue);
            case "absolute-size" -> ABSOLUTE_SIZE_KEYWORDS.contains(lowerValue);
            case "relative-size" -> RELATIVE_SIZE_KEYWORDS.contains(lowerValue);
            case "line-width" -> isLineWidth(lowerValue) || isLength(trimmed);
            case "line-style" -> isLineStyle(lowerValue);
            case "hex-color" -> HEX_COLOR_PATTERN.matcher(trimmed).matches();
            case "named-color" -> NAMED_COLORS.contains(lowerValue);
            case "declaration-value" -> true; // matches anything
            default -> false; // unknown type - don't match
        };
    }

    /**
     * Check if a value matches a global CSS keyword.
     */
    public static boolean isGlobalValue(String value) {
        return GLOBAL_VALUES.contains(value.toLowerCase());
    }

    private static boolean isNumber(String value) {
        return NUMBER_PATTERN.matcher(value).matches();
    }

    private static boolean isInteger(String value) {
        return INTEGER_PATTERN.matcher(value).matches();
    }

    private static boolean isLength(String value) {
        if ("0".equals(value)) {
            return true; // unitless zero is a valid length
        }
        return isNumberWithUnit(value, LENGTH_UNITS);
    }

    private static boolean isPercentage(String value) {
        return value.endsWith("%") && isNumber(value.substring(0, value.length() - 1));
    }

    private static boolean isAngle(String value) {
        if ("0".equals(value)) {
            return true;
        }
        return isNumberWithUnit(value, ANGLE_UNITS);
    }

    private static boolean isTime(String value) {
        return isNumberWithUnit(value, TIME_UNITS);
    }

    private static boolean isResolution(String value) {
        return isNumberWithUnit(value, RESOLUTION_UNITS);
    }

    private static boolean isFrequency(String value) {
        return isNumberWithUnit(value, FREQUENCY_UNITS);
    }

    private static boolean isNumberWithUnit(String value, Set<String> units) {
        for (String unit : units) {
            if (value.endsWith(unit)) {
                String numPart = value.substring(0, value.length() - unit.length());
                if (isNumber(numPart)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isColor(String lowerValue) {
        // Hex color
        if (lowerValue.startsWith("#")) {
            return HEX_COLOR_PATTERN.matcher(lowerValue).matches();
        }
        // Named color
        if (NAMED_COLORS.contains(lowerValue)) {
            return true;
        }
        // Function-based colors
        if (lowerValue.startsWith("rgb(") || lowerValue.startsWith("rgba(")
            || lowerValue.startsWith("hsl(") || lowerValue.startsWith("hsla(")
            || lowerValue.startsWith("hwb(") || lowerValue.startsWith("lab(")
            || lowerValue.startsWith("lch(") || lowerValue.startsWith("oklab(")
            || lowerValue.startsWith("oklch(") || lowerValue.startsWith("color(")) {
            return true;
        }
        return false;
    }

    private static boolean isString(String value) {
        return (value.startsWith("\"") && value.endsWith("\""))
            || (value.startsWith("'") && value.endsWith("'"));
    }

    private static boolean isFunctionCall(String value, String funcName) {
        return value.startsWith(funcName + "(") && value.endsWith(")");
    }

    private static boolean isImage(String lowerValue) {
        return isFunctionCall(lowerValue, "url")
            || isFunctionCall(lowerValue, "image")
            || isFunctionCall(lowerValue, "linear-gradient")
            || isFunctionCall(lowerValue, "radial-gradient")
            || isFunctionCall(lowerValue, "conic-gradient")
            || isFunctionCall(lowerValue, "repeating-linear-gradient")
            || isFunctionCall(lowerValue, "repeating-radial-gradient")
            || isFunctionCall(lowerValue, "repeating-conic-gradient");
    }

    private static boolean isCustomIdent(String value) {
        if (value.isEmpty()) {
            return false;
        }
        // Must not be a CSS-wide keyword
        if (GLOBAL_VALUES.contains(value.toLowerCase())) {
            return false;
        }
        return isIdent(value);
    }

    private static boolean isIdent(String value) {
        if (value.isEmpty()) {
            return false;
        }
        char first = value.charAt(0);
        if (!Character.isLetter(first) && first != '_' && first != '-') {
            return false;
        }
        for (int i = 1; i < value.length(); i++) {
            char c = value.charAt(i);
            if (!Character.isLetterOrDigit(c) && c != '-' && c != '_') {
                return false;
            }
        }
        return true;
    }

    private static boolean isPosition(String lowerValue) {
        // Basic position keywords
        return switch (lowerValue) {
            case "left", "center", "right", "top", "bottom" -> true;
            default -> isLength(lowerValue) || isPercentage(lowerValue);
        };
    }

    private static boolean isLineWidth(String lowerValue) {
        return switch (lowerValue) {
            case "thin", "medium", "thick" -> true;
            default -> false;
        };
    }

    private static boolean isLineStyle(String lowerValue) {
        return switch (lowerValue) {
            case "none", "hidden", "dotted", "dashed", "solid", "double", "groove", "ridge", "inset", "outset" ->
                true;
            default -> false;
        };
    }
}
