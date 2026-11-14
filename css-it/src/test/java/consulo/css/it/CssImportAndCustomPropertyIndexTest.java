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
import consulo.css.lang.index.CssCustomPropertyIndex.Declaration;
import consulo.css.lang.index.CssCustomPropertyIndex;
import consulo.css.lang.index.CssImportIndex;
import consulo.css.lang.psi.CssVariable;
import consulo.css.lang.psi.CssVariableReference;
import consulo.it.HeadlessProjectExtension;
import consulo.it.HeadlessProjects;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiManager;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.project.Project;
import consulo.virtualFileSystem.LocalFileSystem;
import consulo.virtualFileSystem.VirtualFile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Indexes real CSS files in a headless project and queries {@link CssImportIndex} and {@link CssCustomPropertyIndex}
 * the way a framework plugin would, for example to find the entry files of Tailwind CSS and their theme.
 */
@ExtendWith(HeadlessProjectExtension.class)
public class CssImportAndCustomPropertyIndexTest {
    /**
     * The {@code resources/css/app.css} of a new Laravel application
     */
    private static final String APP_CSS = """
        @import 'tailwindcss';

        @source '../../vendor/laravel/framework/src/Illuminate/Pagination/resources/views/*.blade.php';

        @theme {
            --font-sans: 'Instrument Sans', ui-sans-serif, system-ui, sans-serif;
            --color-brand: oklch(0.72 0.11 221.19);
        }
        """;

    private static final String STYLES_CSS = """
        @import url("theme.css") layer(base) supports(display: grid) screen;
        @import "print.css" print;

        :root {
            --color-brand: #0055aa;
        }

        @media (prefers-color-scheme: dark) {
            :root {
                --color-brand: #66aaff;
            }
        }
        """;

    @Test
    public void findsFilesByImportedPath(HeadlessProjects projects) throws Exception {
        Path directory = createFiles(projects);
        Project project = CssIndexTesting.openWithModule(projects, directory);
        GlobalSearchScope scope = GlobalSearchScope.allScope(project);

        List<String> tailwindEntries = CssIndexTesting.waitForIndex(
            () -> CssImportIndex.getFilesImporting("tailwindcss", scope).stream().map(VirtualFile::getName).toList(),
            names -> !names.isEmpty()
        );
        assertThat(tailwindEntries).containsExactly("app.css");
        assertThat(CssIndexTesting.waitForIndex(
            () -> CssImportIndex.getFilesImporting("theme.css", scope).stream().map(VirtualFile::getName).toList(),
            names -> !names.isEmpty()
        )).containsExactly("styles.css");
        assertThat(CssIndexTesting.waitForIndex(
            () -> CssImportIndex.getFilesImporting("print.css", scope).stream().map(VirtualFile::getName).toList(),
            names -> !names.isEmpty()
        )).containsExactly("styles.css");
    }

    /**
     * Each declaration keeps the at-rule it is declared in: {@code @theme} of Tailwind, {@code @media}, or none in a style rule
     */
    @Test
    public void findsDeclarationsWithTheirAtRule(HeadlessProjects projects) throws Exception {
        Path directory = createFiles(projects);
        Project project = CssIndexTesting.openWithModule(projects, directory);
        GlobalSearchScope scope = GlobalSearchScope.allScope(project);

        List<Declaration> brand = CssIndexTesting.waitForIndex(
            () -> CssCustomPropertyIndex.getDeclarations("--color-brand", scope),
            declarations -> declarations.size() == 3
        );
        assertThat(brand).containsExactlyInAnyOrder(
            new Declaration("theme", "oklch(0.72 0.11 221.19)"),
            new Declaration("", "#0055aa"),
            new Declaration("media", "#66aaff")
        );
        assertThat(CssIndexTesting.waitForIndex(
            () -> CssCustomPropertyIndex.getDeclarations("--font-sans", scope),
            declarations -> !declarations.isEmpty()
        )).containsExactly(new Declaration("theme", "'Instrument Sans', ui-sans-serif, system-ui, sans-serif"));
    }

    /**
     * {@code var()} resolves to the declaration of another stylesheet when its own file has none
     */
    @Test
    public void varResolvesAcrossStylesheets(HeadlessProjects projects) throws Exception {
        Path directory = createFiles(projects);
        Files.writeString(directory.resolve("button.css"), ".button { color: var(--font-sans); }");
        Project project = CssIndexTesting.openWithModule(projects, directory);
        VirtualFile button = LocalFileSystem.getInstance().refreshAndFindFileByNioFile(directory.resolve("button.css"));
        GlobalSearchScope scope = GlobalSearchScope.allScope(project);
        CssIndexTesting.waitForIndex(() -> CssCustomPropertyIndex.getDeclarations("--font-sans", scope), declarations -> !declarations.isEmpty());

        PsiElement target = CssIndexTesting.waitForIndex(() -> {
            PsiFile file = PsiManager.getInstance(project).findFile(button);
            CssVariableReference reference = PsiTreeUtil.findChildOfType(file, CssVariableReference.class);
            return reference == null ? null : reference.resolve();
        }, Objects::nonNull);
        assertThat(target).isInstanceOf(CssVariable.class);
        assertThat(ReadAction.compute(() -> target.getContainingFile().getName())).isEqualTo("app.css");
    }

    private static Path createFiles(HeadlessProjects projects) throws Exception {
        Path directory = projects.newDirectory();
        Files.writeString(directory.resolve("app.css"), APP_CSS);
        Files.writeString(directory.resolve("styles.css"), STYLES_CSS);
        return directory;
    }
}
