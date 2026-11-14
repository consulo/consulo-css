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
package consulo.xstylesheet.psi.index;

import consulo.annotation.access.RequiredReadAction;
import consulo.annotation.component.ComponentScope;
import consulo.annotation.component.ExtensionAPI;
import consulo.application.Application;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.project.Project;
import consulo.xstylesheet.psi.XStyleSheetSimpleSelector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The class selectors ({@code .name}) of the stylesheets of a project, for the class attributes of HTML that resolve to
 * them. Each stylesheet language with an index of its class selectors provides one, for example CSS and the languages
 * built on it.
 *
 * @since 2026-11-06
 */
@ExtensionAPI(ComponentScope.APPLICATION)
public interface XStyleSheetClassNameIndex {
    /**
     * @param className the class name, without its leading dot
     * @return the class selectors in {@code scope} declaring {@code className}
     */
    @RequiredReadAction
    List<XStyleSheetSimpleSelector> findClassSelectors(Project project, String className, GlobalSearchScope scope);

    /**
     * @return the class names declared in {@code scope}, without their leading dot
     */
    @RequiredReadAction
    Set<String> getAllClassNames(Project project, GlobalSearchScope scope);

    @RequiredReadAction
    static List<XStyleSheetSimpleSelector> findAllClassSelectors(Project project, String className, GlobalSearchScope scope) {
        List<XStyleSheetSimpleSelector> result = new ArrayList<>();
        Application.get().getExtensionPoint(XStyleSheetClassNameIndex.class)
            .forEach(index -> result.addAll(index.findClassSelectors(project, className, scope)));
        return result;
    }

    @RequiredReadAction
    static Set<String> getAllClassNamesOfAll(Project project, GlobalSearchScope scope) {
        Set<String> result = new HashSet<>();
        Application.get().getExtensionPoint(XStyleSheetClassNameIndex.class)
            .forEach(index -> result.addAll(index.getAllClassNames(project, scope)));
        return result;
    }
}
