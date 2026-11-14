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
package consulo.css.it;

import consulo.application.ReadAction;
import consulo.css.lang.index.CssClassIndex;
import consulo.it.HeadlessProjectExtension;
import consulo.it.HeadlessProjects;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.project.Project;
import consulo.virtualFileSystem.LocalFileSystem;
import consulo.virtualFileSystem.VirtualFile;
import consulo.xstylesheet.psi.XStyleSheetSimpleSelector;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Indexes real CSS and SCSS files in a headless project and queries {@link CssClassIndex} the way a completion
 * contributor would.
 */
@ExtendWith(HeadlessProjectExtension.class)
public class CssClassIndexTest {
    private static final String STYLES_CSS = """
        .card, div.badge.badge-large:hover > .badge-icon {
            color: red;
        }

        #main {
            width: 0.5em;
            background-image: url(images/logo.png);
        }

        @media only screen and (max-width: 600px) {
            .card-compact {
                width: 100%;
            }
        }
        """;

    private static final String THEME_SCSS = """
        .toolbar {
            .toolbar-item {
                color: blue;
            }
        }
        """;

    /**
     * Tags, ids and the dots inside property values are not classes; the classes of a compound selector
     * ({@code div.badge.badge-large}), of a media rule and of a nested SCSS rule are.
     */
    @Test
    public void indexesClassSelectorsOfCssAndScss(HeadlessProjects projects) throws Exception {
        Path directory = projects.newDirectory();
        Files.writeString(directory.resolve("styles.css"), STYLES_CSS);
        Files.writeString(directory.resolve("theme.scss"), THEME_SCSS);

        Project project = CssIndexTesting.openWithModule(projects, directory);
        GlobalSearchScope scope = GlobalSearchScope.allScope(project);

        Set<String> classNames = CssIndexTesting.waitForIndex(() -> CssClassIndex.getAllClassNames(project, scope), names -> !names.isEmpty());
        assertThat(classNames)
            .containsExactlyInAnyOrder("card", "badge", "badge-large", "badge-icon", "card-compact", "toolbar", "toolbar-item");
    }

    @Test
    public void findsTheFilesAndSelectorsOfAClass(HeadlessProjects projects) throws Exception {
        Path directory = projects.newDirectory();
        Files.writeString(directory.resolve("styles.css"), STYLES_CSS);
        Files.writeString(directory.resolve("theme.scss"), THEME_SCSS);

        Project project = CssIndexTesting.openWithModule(projects, directory);
        GlobalSearchScope scope = GlobalSearchScope.allScope(project);

        List<String> files = CssIndexTesting.waitForIndex(
            () -> CssClassIndex.getFilesWithClass("toolbar-item", scope).stream().map(VirtualFile::getName).toList(),
            names -> !names.isEmpty()
        );
        assertThat(files).containsExactly("theme.scss");

        List<XStyleSheetSimpleSelector> selectors = ReadAction.compute(() -> CssClassIndex.findClassSelectors(project, "badge-large", scope));
        assertThat(selectors).hasSize(1);
        assertThat(ReadAction.compute(() -> selectors.get(0).getName())).isEqualTo("badge-large");
        assertThat(ReadAction.compute(() -> selectors.get(0).getContainingFile().getName())).isEqualTo("styles.css");
    }

    /**
     * A key outlives the files declaring it in the project-wide key set; a scope that excludes those files must not
     * report it.
     */
    @Test
    public void allClassNamesAreLimitedToTheScope(HeadlessProjects projects) throws Exception {
        Path directory = projects.newDirectory();
        Files.writeString(directory.resolve("styles.css"), STYLES_CSS);
        Files.writeString(directory.resolve("theme.scss"), THEME_SCSS);

        Project project = CssIndexTesting.openWithModule(projects, directory);
        VirtualFile styles = LocalFileSystem.getInstance().refreshAndFindFileByNioFile(directory.resolve("styles.css"));
        assertThat(styles).isNotNull();
        GlobalSearchScope stylesOnly = GlobalSearchScope.fileScope(project, styles);

        Set<String> classNames = CssIndexTesting.waitForIndex(() -> CssClassIndex.getAllClassNames(project, stylesOnly), names -> !names.isEmpty());
        assertThat(classNames).contains("card").doesNotContain("toolbar", "toolbar-item");
    }

    @Test
    public void followsExternalChanges(HeadlessProjects projects) throws Exception {
        Path directory = projects.newDirectory();
        Path styles = directory.resolve("styles.css");
        Files.writeString(styles, ".old-name { color: red; }");

        Project project = CssIndexTesting.openWithModule(projects, directory);
        GlobalSearchScope scope = GlobalSearchScope.allScope(project);

        CssIndexTesting.waitForIndex(() -> CssClassIndex.getAllClassNames(project, scope), names -> names.contains("old-name"));

        // an external tool renames the class
        Files.writeString(styles, ".new-name { color: red; }");
        VirtualFile stylesFile = LocalFileSystem.getInstance().refreshAndFindFileByNioFile(styles);
        assertThat(stylesFile).isNotNull();
        stylesFile.refresh(false, false);

        Set<String> classNames = CssIndexTesting.waitForIndex(() -> CssClassIndex.getAllClassNames(project, scope), names -> names.contains("new-name"));
        assertThat(classNames).containsExactly("new-name");
    }
}
