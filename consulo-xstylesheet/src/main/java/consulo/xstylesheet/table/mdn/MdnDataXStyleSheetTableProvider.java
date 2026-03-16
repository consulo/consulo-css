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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.psi.PsiFile;
import consulo.logging.Logger;
import consulo.util.lang.lazy.LazyValue;
import consulo.xstylesheet.definition.XStyleSheetTable;
import consulo.xstylesheet.definition.XStyleSheetTableProvider;
import consulo.xstylesheet.definition.impl.EmptyXStyleSheetTable;

import org.jspecify.annotations.Nullable;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Provides CSS property definitions from MDN (Mozilla Developer Network) data.
 * <p>
 * This provider loads the {@code properties.json}, {@code syntaxes.json}, and
 * {@code at-rules.json} files from the bundled mdn-data resources and creates
 * an {@link XStyleSheetTable} with full property validation support using the
 * CSS Value Definition Syntax.
 * <p>
 * At-rule descriptors (e.g. {@code src} in {@code @font-face}) are also loaded
 * from {@code at-rules.json} and added to the property table.
 *
 * @author VISTALL
 * @since 2026-03-15
 */
@ExtensionImpl(order = "first")
public class MdnDataXStyleSheetTableProvider implements XStyleSheetTableProvider {
    private static final Logger LOGGER = Logger.getInstance(MdnDataXStyleSheetTableProvider.class);

    /**
     * Property name prefixes to skip (vendor-prefixed and internal properties).
     */
    private static final Set<String> SKIP_PREFIXES = Set.of(
        "-ms-", "-moz-", "-webkit-", "-o-"
    );

    private final Supplier<XStyleSheetTable> tableValue = LazyValue.notNull(this::loadTable);

    @Nullable
    @Override
    public XStyleSheetTable getTableForFile(PsiFile file) {
        return tableValue.get();
    }

    private XStyleSheetTable loadTable() {
        try {
            JsonObject propertiesJson = loadJsonResource("/consulo/xstylesheet/mdn/properties.json");
            JsonObject syntaxesJson = loadJsonResource("/consulo/xstylesheet/mdn/syntaxes.json");
            JsonObject atRulesJson = loadJsonResource("/consulo/xstylesheet/mdn/at-rules.json");

            if (propertiesJson == null || syntaxesJson == null) {
                LOGGER.error("Failed to load MDN data JSON resources");
                return EmptyXStyleSheetTable.INSTANCE;
            }

            CssValueSyntaxParser parser = new CssValueSyntaxParser();

            // Parse syntaxes
            Map<String, CssValueSyntaxNode> syntaxesMap = new LinkedHashMap<>();
            for (Map.Entry<String, JsonElement> entry : syntaxesJson.entrySet()) {
                String name = entry.getKey();
                JsonObject obj = entry.getValue().getAsJsonObject();
                String syntax = obj.has("syntax") ? obj.get("syntax").getAsString() : "";
                if (!syntax.isEmpty()) {
                    try {
                        syntaxesMap.put(name, parser.parse(syntax));
                    }
                    catch (Exception e) {
                        LOGGER.warn("Failed to parse syntax for type '" + name + "': " + syntax, e);
                    }
                }
            }

            // Parse properties
            Map<String, MdnXStyleSheetProperty> propertiesMap = new LinkedHashMap<>();
            for (Map.Entry<String, JsonElement> entry : propertiesJson.entrySet()) {
                String propertyName = entry.getKey();

                // Skip vendor-prefixed properties
                if (shouldSkipProperty(propertyName)) {
                    continue;
                }

                JsonObject obj = entry.getValue().getAsJsonObject();
                String syntax = obj.has("syntax") ? obj.get("syntax").getAsString() : "";
                String status = obj.has("status") ? obj.get("status").getAsString() : "standard";

                // Skip non-standard properties
                if ("nonstandard".equals(status)) {
                    continue;
                }

                String initial = obj.has("initial") ? getInitialValue(obj.get("initial")) : "";
                boolean inherited = obj.has("inherited") && obj.get("inherited").getAsBoolean();
                String mdnUrl = obj.has("mdn_url") ? obj.get("mdn_url").getAsString() : "";

                addProperty(parser, syntaxesMap, propertiesMap, propertyName, syntax, initial, inherited, mdnUrl);
            }

            // Parse at-rule descriptors (e.g. @font-face src, font-display, etc.)
            if (atRulesJson != null) {
                loadAtRuleDescriptors(atRulesJson, parser, syntaxesMap, propertiesMap);
            }

            LOGGER.info("Loaded " + propertiesMap.size() + " CSS properties from MDN data");
            return new MdnXStyleSheetTable(propertiesMap);
        }
        catch (Exception e) {
            LOGGER.error("Failed to load MDN CSS data", e);
            return EmptyXStyleSheetTable.INSTANCE;
        }
    }

    private void loadAtRuleDescriptors(
        JsonObject atRulesJson,
        CssValueSyntaxParser parser,
        Map<String, CssValueSyntaxNode> syntaxesMap,
        Map<String, MdnXStyleSheetProperty> propertiesMap
    ) {
        for (Map.Entry<String, JsonElement> ruleEntry : atRulesJson.entrySet()) {
            JsonObject ruleObj = ruleEntry.getValue().getAsJsonObject();
            if (!ruleObj.has("descriptors")) {
                continue;
            }

            JsonObject descriptors = ruleObj.getAsJsonObject("descriptors");
            for (Map.Entry<String, JsonElement> descEntry : descriptors.entrySet()) {
                String descriptorName = descEntry.getKey();

                // Don't overwrite if already defined as a regular property
                if (propertiesMap.containsKey(descriptorName)) {
                    continue;
                }

                JsonObject descObj = descEntry.getValue().getAsJsonObject();
                String syntax = descObj.has("syntax") ? descObj.get("syntax").getAsString() : "";
                String initial = descObj.has("initial") ? getInitialValue(descObj.get("initial")) : "";

                addProperty(parser, syntaxesMap, propertiesMap, descriptorName, syntax, initial, false, "");
            }
        }
    }

    private void addProperty(
        CssValueSyntaxParser parser,
        Map<String, CssValueSyntaxNode> syntaxesMap,
        Map<String, MdnXStyleSheetProperty> propertiesMap,
        String propertyName,
        String syntax,
        String initial,
        boolean inherited,
        String mdnUrl
    ) {
        CssValueSyntaxNode syntaxNode;
        try {
            syntaxNode = parser.parse(syntax);
        }
        catch (Exception e) {
            LOGGER.warn("Failed to parse syntax for property '" + propertyName + "': " + syntax, e);
            syntaxNode = parser.parse("");
        }

        MdnXStyleSheetProperty property = new MdnXStyleSheetProperty(
            propertyName,
            syntax,
            initial,
            inherited,
            mdnUrl,
            syntaxNode,
            syntaxesMap,
            propertiesMap
        );
        propertiesMap.put(propertyName, property);
    }

    private boolean shouldSkipProperty(String name) {
        for (String prefix : SKIP_PREFIXES) {
            if (name.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    private String getInitialValue(JsonElement element) {
        if (element.isJsonPrimitive()) {
            return element.getAsString();
        }
        // Some properties have arrays for initial values (shorthand properties)
        return "";
    }

    @Nullable
    private JsonObject loadJsonResource(String resourcePath) {
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is == null) {
                LOGGER.error("Resource not found: " + resourcePath);
                return null;
            }
            try (Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
                return JsonParser.parseReader(reader).getAsJsonObject();
            }
        }
        catch (Exception e) {
            LOGGER.error("Failed to load JSON resource: " + resourcePath, e);
            return null;
        }
    }
}
