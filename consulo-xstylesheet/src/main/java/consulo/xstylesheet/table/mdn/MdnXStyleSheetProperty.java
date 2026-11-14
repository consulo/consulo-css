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
import consulo.ui.util.ColorValueUtil;
import consulo.ui.image.ImageEffects;
import consulo.xstylesheet.color.CssColorParser;
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
                // <color> expands to the named colors: a color for the gutter icon and the color picker
                XStyleSheetColor color = XStyleSheetColor.getColor(keyword.toLowerCase());
                if (color != null) {
                    return ColorValueUtil.fromHex("#" + color.colorCode());
                }
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
                    ColorValue colorValue = CssColorParser.parse(text);
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
                // rgb(), hsl(), oklab(), oklch(): a color for the gutter icon and the color picker
                ColorValue color = isColorFunction() ? CssColorParser.parse(text) : null;
                return color != null ? color : text;
            }
            return null;
        }

        private boolean isColorFunction() {
            return switch (functionName) {
                case "rgb", "rgba", "hsl", "hsla", "oklab", "oklch" -> true;
                default -> false;
            };
        }

        @Override
        public List<LookupElement> getLookupElements(String value) {
            return List.of(LookupElementBuilder.create(functionName + "()"));
        }
    }
}
