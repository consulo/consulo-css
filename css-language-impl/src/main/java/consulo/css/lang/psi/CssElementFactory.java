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

package consulo.css.lang.psi;

import consulo.xstylesheet.psi.PsiXStyleSheetPropertyValuePart;
import consulo.language.Language;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiFileFactory;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.project.Project;
import consulo.xstylesheet.psi.PsiXStyleSheetRule;
import consulo.xstylesheet.psi.XStyleSheetSelectorList;

import java.util.Objects;

/**
 * Creates CSS PSI from text, in CSS or in a dialect language.
 *
 * @since 2026-10-26
 */
public final class CssElementFactory {
    private final Project myProject;

    private CssElementFactory(Project project) {
        myProject = project;
    }

    public static CssElementFactory getInstance(Project project) {
        return new CssElementFactory(project);
    }

    public XStyleSheetSelectorList createSelectorList(String text, Language language) {
        PsiFile file = createFile(text + " {}", language);
        return Objects.requireNonNull(PsiTreeUtil.findChildOfType(file, XStyleSheetSelectorList.class));
    }

    public PsiXStyleSheetRule createRuleset(String text, Language language) {
        PsiFile file = createFile(text, language);
        return Objects.requireNonNull(PsiTreeUtil.findChildOfType(file, PsiXStyleSheetRule.class));
    }

    /**
     * The value part of {@code text} ({@code #fff}, {@code rgba(0, 0, 0, 50%)}).
     */
    public PsiXStyleSheetPropertyValuePart createPropertyValuePart(String text, Language language) {
        PsiFile file = createFile("a { b: " + text + " }", language);
        return Objects.requireNonNull(PsiTreeUtil.findChildOfType(file, PsiXStyleSheetPropertyValuePart.class));
    }

    private PsiFile createFile(String text, Language language) {
        return PsiFileFactory.getInstance(myProject).createFileFromText("dummy.css", language, text);
    }
}
