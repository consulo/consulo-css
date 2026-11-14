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
import consulo.application.WriteAction;
import consulo.application.dumb.IndexNotReadyException;
import consulo.it.HeadlessProjects;
import consulo.module.ModifiableModuleModel;
import consulo.module.Module;
import consulo.module.ModuleManager;
import consulo.module.content.ModuleRootManager;
import consulo.module.content.layer.ModifiableRootModel;
import consulo.project.Project;
import consulo.virtualFileSystem.LocalFileSystem;
import consulo.virtualFileSystem.VirtualFile;

import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Opens a headless project with a module over a directory and waits for the CSS indexes
 */
final class CssIndexTesting {
    private static final long TIMEOUT_SECONDS = 60;

    private CssIndexTesting() {
    }

    static Project openWithModule(HeadlessProjects projects, Path directory) throws Exception {
        Project project = projects.open(directory);

        VirtualFile directoryFile = LocalFileSystem.getInstance().refreshAndFindFileByNioFile(directory);
        assertThat(directoryFile).isNotNull();

        ModuleManager moduleManager = ModuleManager.getInstance(project);
        WriteAction.run(() -> {
            ModifiableModuleModel moduleModel = moduleManager.getModifiableModel();
            Module module = moduleModel.newModule("main", directory.toString());
            moduleModel.commit();

            ModifiableRootModel rootModel = ModuleRootManager.getInstance(module).getModifiableModel();
            rootModel.addContentEntry(directoryFile);
            rootModel.commit();
        });

        HeadlessProjects.awaitIdle(project);
        return project;
    }

    /**
     * Polls {@code query} in a read action until its answer satisfies {@code ready}. The index refuses to answer while a
     * reindex runs in dumb mode, which the poll waits out as well.
     */
    static <T> T waitForIndex(Supplier<T> query, Predicate<T> ready) throws Exception {
        long deadline = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(TIMEOUT_SECONDS);
        T answer = null;
        while (System.currentTimeMillis() < deadline) {
            answer = ReadAction.compute(() -> {
                try {
                    return query.get();
                }
                catch (IndexNotReadyException e) {
                    return null;
                }
            });
            if (answer != null && ready.test(answer)) {
                return answer;
            }
            Thread.sleep(50);
        }
        throw new AssertionError("timed out waiting for the index, last answer: " + answer);
    }
}
