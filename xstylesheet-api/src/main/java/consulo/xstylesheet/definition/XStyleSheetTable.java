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

package consulo.xstylesheet.definition;

import org.jspecify.annotations.Nullable;
import java.util.Collection;
import java.util.List;

/**
 * @author VISTALL
 * @since 2013-07-03
 */
public interface XStyleSheetTable {
    @Nullable
    XStyleSheetProperty findProperty(String propertyName);

    Collection<XStyleSheetProperty> getProperties();

    /**
     * @return the at-rules, for example those of CSS or of a CSS framework
     */
    default Collection<XStyleSheetAtRule> getAtRules() {
        return List.of();
    }

    default @Nullable XStyleSheetAtRule findAtRule(String name) {
        for (XStyleSheetAtRule atRule : getAtRules()) {
            if (atRule.name().equals(name)) {
                return atRule;
            }
        }
        return null;
    }
}
