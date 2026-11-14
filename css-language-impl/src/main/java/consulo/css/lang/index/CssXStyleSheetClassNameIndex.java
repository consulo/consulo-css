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
package consulo.css.lang.index;

import consulo.annotation.access.RequiredReadAction;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.project.Project;
import consulo.xstylesheet.psi.XStyleSheetSimpleSelector;
import consulo.xstylesheet.psi.index.XStyleSheetClassNameIndex;

import java.util.List;
import java.util.Set;

/**
 * The class selectors of CSS and of the languages built on it, from {@link CssClassIndex}
 *
 * @since 2026-11-06
 */
@ExtensionImpl
public class CssXStyleSheetClassNameIndex implements XStyleSheetClassNameIndex {
    @RequiredReadAction
    @Override
    public List<XStyleSheetSimpleSelector> findClassSelectors(Project project, String className, GlobalSearchScope scope) {
        return CssClassIndex.findClassSelectors(project, className, scope);
    }

    @RequiredReadAction
    @Override
    public Set<String> getAllClassNames(Project project, GlobalSearchScope scope) {
        return CssClassIndex.getAllClassNames(project, scope);
    }
}
