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

import consulo.language.editor.completion.lookup.LookupElement;
import consulo.language.editor.completion.lookup.LookupElementBuilder;
import consulo.language.editor.rawHighlight.HighlightInfo;
import consulo.ui.color.ColorValue;
import consulo.ui.color.RGBColor;
import consulo.ui.image.ImageEffects;
import consulo.ui.util.ColorValueUtil;
import consulo.xstylesheet.definition.XStyleSheetColor;
import consulo.xstylesheet.definition.XStyleSheetProperty;
import consulo.xstylesheet.definition.XStyleSheetPropertyValueEntry;
import consulo.xstylesheet.definition.XStyleSheetPropertyValuePart;
import consulo.xstylesheet.definition.XStyleSheetPropertyValuePartParser;
import consulo.xstylesheet.psi.PsiXStyleSheetPropertyValuePart;

import org.jspecify.annotations.Nullable;
import java.util.*;

/**
 * An {@link XStyleSheetProperty} implementation backed by MDN data.
 * <p>
 * Uses the parsed CSS Value Definition Syntax AST to validate property values
 * and provide completion suggestions.
 *
 * @author VISTALL
 * @since 2026-03-15
 */
public class MdnXStyleSheetProperty implements XStyleSheetProperty {
    private final String name;
    private final String syntax;
    private final String initial;
    private final boolean inherited;
    private final String mdnUrl;
    private final CssValueSyntaxNode syntaxNode;
    private final Map<String, CssValueSyntaxNode> syntaxesMap;
    private final Map<String, MdnXStyleSheetProperty> propertiesMap;

    private volatile XStyleSheetPropertyValueEntry[] cachedValidEntries;

    public MdnXStyleSheetProperty(
        String name,
        String syntax,
        String initial,
        boolean inherited,
        String mdnUrl,
        CssValueSyntaxNode syntaxNode,
        Map<String, CssValueSyntaxNode> syntaxesMap,
        Map<String, MdnXStyleSheetProperty> propertiesMap
    ) {
        this.name = name;
        this.syntax = syntax;
        this.initial = initial;
        this.inherited = inherited;
        this.mdnUrl = mdnUrl;
        this.syntaxNode = syntaxNode;
        this.syntaxesMap = syntaxesMap;
        this.propertiesMap = propertiesMap;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public XStyleSheetPropertyValueEntry[] getValidEntries() {
        if (cachedValidEntries == null) {
            cachedValidEntries = buildValidEntries();
        }
        return cachedValidEntries;
    }

    @Override
    public XStyleSheetPropertyValueEntry[] getInitialEntries() {
        if (initial == null || initial.isEmpty()) {
            return XStyleSheetPropertyValueEntry.EMPTY_ARRAY;
        }

        // Split by space for multi-value defaults (e.g. "medium none currentcolor" for border)
        // but return as single entry with the first token as value
        String initialValue = initial.trim();

        XStyleSheetPropertyValuePart part = new MdnPropertyValuePart(new KeywordPartParser(initialValue), initialValue);
        XStyleSheetPropertyValueEntry entry = new MdnPropertyValueEntry(new XStyleSheetPropertyValuePart[]{part});
        return new XStyleSheetPropertyValueEntry[]{entry};
    }

    /**
     * Build valid entries from the syntax AST.
     * <p>
     * This creates a single entry containing all valid value parts extracted from the syntax tree.
     * The entry's parts include keyword validators and data type validators.
     */
    private XStyleSheetPropertyValueEntry[] buildValidEntries() {
        List<XStyleSheetPropertyValuePart> parts = new ArrayList<>();
        collectValueParts(syntaxNode, parts, new HashSet<>());

        // Always add global CSS values
        for (String global : List.of("inherit", "initial", "unset", "revert", "revert-layer")) {
            parts.add(createKeywordPart(global));
        }

        if (parts.isEmpty()) {
            return XStyleSheetPropertyValueEntry.EMPTY_ARRAY;
        }

        XStyleSheetPropertyValueEntry entry = new MdnPropertyValueEntry(parts.toArray(XStyleSheetPropertyValuePart.EMPTY_ARRAY));
        return new XStyleSheetPropertyValueEntry[]{entry};
    }

    /**
     * Recursively collect value parts from the syntax AST.
     */
    private void collectValueParts(CssValueSyntaxNode node, List<XStyleSheetPropertyValuePart> parts, Set<String> visited) {
        if (node instanceof CssValueSyntaxNode.Keyword kw) {
            if (!kw.value().isEmpty()) {
                parts.add(createKeywordPart(kw.value()));
            }
        }
        else if (node instanceof CssValueSyntaxNode.DataType dt) {
            String typeName = dt.name();
            // Try to resolve the type through syntaxes map
            if (syntaxesMap.containsKey(typeName) && !visited.contains(typeName)) {
                visited.add(typeName);
                collectValueParts(syntaxesMap.get(typeName), parts, visited);
                visited.remove(typeName);
            }
            // Also add a data type matcher part
            parts.add(createDataTypePart(typeName));
        }
        else if (node instanceof CssValueSyntaxNode.PropertyRef ref) {
            MdnXStyleSheetProperty refProp = propertiesMap.get(ref.propertyName());
            if (refProp != null && !visited.contains(ref.propertyName())) {
                visited.add(ref.propertyName());
                collectValueParts(refProp.syntaxNode, parts, visited);
                visited.remove(ref.propertyName());
            }
        }
        else if (node instanceof CssValueSyntaxNode.Function func) {
            parts.add(createFunctionPart(func.name()));
        }
        else if (node instanceof CssValueSyntaxNode.Group group) {
            for (CssValueSyntaxNode child : group.children()) {
                collectValueParts(child, parts, visited);
            }
        }
        else if (node instanceof CssValueSyntaxNode.Multiplied mult) {
            collectValueParts(mult.child(), parts, visited);
        }
    }

    private static XStyleSheetPropertyValuePart createKeywordPart(String keyword) {
        return new MdnPropertyValuePart(new KeywordPartParser(keyword), keyword);
    }

    private static XStyleSheetPropertyValuePart createDataTypePart(String dataType) {
        return new MdnPropertyValuePart(new DataTypePartParser(dataType), null);
    }

    private static XStyleSheetPropertyValuePart createFunctionPart(String functionName) {
        return new MdnPropertyValuePart(new FunctionPartParser(functionName), null);
    }

    @Override
    public String getSyntax() {
        return syntax;
    }

    @Override
    public String getInitialValue() {
        return initial;
    }

    @Override
    public boolean isInherited() {
        return inherited;
    }

    @Override
    public String getMdnUrl() {
        return mdnUrl;
    }

    // -- Inner records --

    private record MdnPropertyValueEntry(XStyleSheetPropertyValuePart[] parts) implements XStyleSheetPropertyValueEntry {
        @Override
        public XStyleSheetPropertyValuePart[] getParts() {
            return parts;
        }
    }

    private record MdnPropertyValuePart(XStyleSheetPropertyValuePartParser parser, String value) implements XStyleSheetPropertyValuePart {
        @Override
        public XStyleSheetPropertyValuePartParser getParser() {
            return parser;
        }

        @Override
        public List<HighlightInfo> createHighlights(PsiXStyleSheetPropertyValuePart valuePart) {
            return parser.createHighlights(valuePart);
        }

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public Object getNativeValue(PsiXStyleSheetPropertyValuePart part) {
            return parser.getNativeValue(part, value);
        }

        @Override
        public List<LookupElement> getLookupElements() {
            return parser.getLookupElements(value);
        }
    }

    /**
     * Parser that matches exact keyword values.
     */
    private record KeywordPartParser(String keyword) implements XStyleSheetPropertyValuePartParser {
        @Override
        @Nullable
        public Object getNativeValue(PsiXStyleSheetPropertyValuePart valuePart, String value) {
            String text = valuePart.getText().trim();
            if (keyword.equalsIgnoreCase(text)) {
                return keyword;
            }
            return null;
        }

        @Override
        public List<LookupElement> getLookupElements(String value) {
            return List.of(LookupElementBuilder.create(keyword));
        }
    }

    /**
     * Parser that matches CSS data types like {@code <length>}, {@code <color>}, etc.
     * <p>
     * For color-related data types, returns a {@link ColorValue} so that
     * {@link consulo.xstylesheet.highlight.XStyleSheetElementColorProvider}
     * can display gutter color icons.
     */
    private record DataTypePartParser(String dataType) implements XStyleSheetPropertyValuePartParser {
        @Override
        @Nullable
        public Object getNativeValue(PsiXStyleSheetPropertyValuePart valuePart, String value) {
            String text = valuePart.getText().trim();
            if (CssPrimitiveValueMatcher.matchesDataType(dataType, text)) {
                // For color-related types, return ColorValue for gutter icon support
                if (isColorType()) {
                    ColorValue colorValue = parseColorValue(text);
                    if (colorValue != null) {
                        return colorValue;
                    }
                }
                return text;
            }
            return null;
        }

        @Override
        public List<LookupElement> getLookupElements(String value) {
            if (isColorType()) {
                List<LookupElement> list = new ArrayList<>();
                for (XStyleSheetColor entry : XStyleSheetColor.values()) {
                    String hex = "#" + entry.colorCode();
                    LookupElementBuilder builder = LookupElementBuilder.create(entry.name())
                        .withIcon(ImageEffects.colorFilled(16, 16, ColorValueUtil.fromHex(hex)))
                        .withTypeText(hex, true);
                    list.add(builder);
                }
                return list;
            }
            return List.of();
        }

        private boolean isColorType() {
            return "color".equals(dataType) || "hex-color".equals(dataType) || "named-color".equals(dataType);
        }

        @Nullable
        private static ColorValue parseColorValue(String text) {
            String lower = text.toLowerCase();
            // Hex color
            if (lower.startsWith("#")) {
                try {
                    return ColorValueUtil.fromHex(text);
                }
                catch (Exception e) {
                    return null;
                }
            }
            // Named color
            XStyleSheetColor color = XStyleSheetColor.getColor(lower);
            if (color != null) {
                try {
                    return ColorValueUtil.fromHex("#" + color.colorCode());
                }
                catch (Exception e) {
                    return null;
                }
            }
            // rgb() / rgba()
            if (lower.startsWith("rgb")) {
                return parseRgbColor(lower);
            }
            // hsl() / hsla()
            if (lower.startsWith("hsl")) {
                return parseHslColor(lower);
            }
            return null;
        }

        @Nullable
        private static ColorValue parseRgbColor(String lower) {
            try {
                int start = lower.indexOf('(');
                int end = lower.lastIndexOf(')');
                if (start < 0 || end < 0) {
                    return null;
                }
                String[] parts = lower.substring(start + 1, end).split("[,/\\s]+");
                if (parts.length < 3) {
                    return null;
                }
                int r = parseColorComponent(parts[0].trim(), 255);
                int g = parseColorComponent(parts[1].trim(), 255);
                int b = parseColorComponent(parts[2].trim(), 255);
                if (parts.length >= 4) {
                    float a = parseAlpha(parts[3].trim());
                    return new RGBColor(r, g, b, a);
                }
                return new RGBColor(r, g, b);
            }
            catch (Exception e) {
                return null;
            }
        }

        @Nullable
        private static ColorValue parseHslColor(String lower) {
            try {
                int start = lower.indexOf('(');
                int end = lower.lastIndexOf(')');
                if (start < 0 || end < 0) {
                    return null;
                }
                String[] parts = lower.substring(start + 1, end).split("[,/\\s]+");
                if (parts.length < 3) {
                    return null;
                }
                float h = Float.parseFloat(parts[0].trim().replace("deg", "")) / 360f;
                float s = parsePercent(parts[1].trim());
                float l = parsePercent(parts[2].trim());

                // HSL to RGB conversion
                float r, g, b;
                if (s == 0) {
                    r = g = b = l;
                }
                else {
                    float q = l < 0.5f ? l * (1 + s) : l + s - l * s;
                    float p = 2 * l - q;
                    r = hueToRgb(p, q, h + 1f / 3f);
                    g = hueToRgb(p, q, h);
                    b = hueToRgb(p, q, h - 1f / 3f);
                }

                int ri = Math.round(r * 255);
                int gi = Math.round(g * 255);
                int bi = Math.round(b * 255);

                if (parts.length >= 4) {
                    float a = parseAlpha(parts[3].trim());
                    return new RGBColor(ri, gi, bi, a);
                }
                return new RGBColor(ri, gi, bi);
            }
            catch (Exception e) {
                return null;
            }
        }

        private static float hueToRgb(float p, float q, float t) {
            if (t < 0) t += 1;
            if (t > 1) t -= 1;
            if (t < 1f / 6f) return p + (q - p) * 6 * t;
            if (t < 1f / 2f) return q;
            if (t < 2f / 3f) return p + (q - p) * (2f / 3f - t) * 6;
            return p;
        }

        private static int parseColorComponent(String value, int max) {
            if (value.endsWith("%")) {
                float pct = Float.parseFloat(value.substring(0, value.length() - 1));
                return Math.round(pct / 100f * max);
            }
            return Math.min(Math.max(Math.round(Float.parseFloat(value)), 0), max);
        }

        private static float parseAlpha(String value) {
            if (value.endsWith("%")) {
                return Float.parseFloat(value.substring(0, value.length() - 1)) / 100f;
            }
            return Float.parseFloat(value);
        }

        private static float parsePercent(String value) {
            if (value.endsWith("%")) {
                return Float.parseFloat(value.substring(0, value.length() - 1)) / 100f;
            }
            return Float.parseFloat(value);
        }
    }

    /**
     * Parser that matches CSS function calls like url(), calc(), etc.
     */
    private record FunctionPartParser(String functionName) implements XStyleSheetPropertyValuePartParser {
        @Override
        @Nullable
        public Object getNativeValue(PsiXStyleSheetPropertyValuePart valuePart, String value) {
            String text = valuePart.getText().trim();
            if (text.startsWith(functionName + "(")) {
                return text;
            }
            return null;
        }

        @Override
        public List<LookupElement> getLookupElements(String value) {
            return List.of(LookupElementBuilder.create(functionName + "()"));
        }
    }
}
