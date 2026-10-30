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

package consulo.xstylesheet.inspection;

import consulo.annotation.component.ComponentScope;
import consulo.annotation.component.ExtensionAPI;
import consulo.component.extension.ExtensionPointName;
import consulo.language.psi.PsiElement;
import consulo.xstylesheet.psi.PsiXStyleSheetProperty;

/**
 * Lets stylesheet dialects switch off CSS inspections where their syntax would be reported as invalid CSS.
 *
 * @since 2026-10-26
 */
@ExtensionAPI(ComponentScope.APPLICATION)
public interface CssInspectionFilter {
    ExtensionPointName<CssInspectionFilter> EP_NAME = ExtensionPointName.create(CssInspectionFilter.class);

    /**
     * @param inspectionClass class of a CSS inspection
     */
    default boolean isSupported(Class<?> inspectionClass, PsiElement context) {
        return true;
    }

    /**
     * Whether the value of the property should be checked against the CSS property definitions,
     * e.g. not when it contains dialect variables ({@code $var}).
     */
    default boolean isValueShouldBeValidatedWithCssScheme(PsiXStyleSheetProperty property) {
        return true;
    }

    static boolean isInspectionSupported(Class<?> inspectionClass, PsiElement context) {
        for (CssInspectionFilter filter : EP_NAME.getExtensionList()) {
            if (!filter.isSupported(inspectionClass, context)) {
                return false;
            }
        }
        return true;
    }

    static boolean isValueValidated(PsiXStyleSheetProperty property) {
        for (CssInspectionFilter filter : EP_NAME.getExtensionList()) {
            if (!filter.isValueShouldBeValidatedWithCssScheme(property)) {
                return false;
            }
        }
        return true;
    }
}
