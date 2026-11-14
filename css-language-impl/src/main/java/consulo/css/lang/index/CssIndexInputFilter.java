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

import consulo.css.lang.CssLanguage;
import consulo.language.file.LanguageFileType;
import consulo.language.psi.stub.FileBasedIndex;
import consulo.project.Project;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.fileType.FileType;
import consulo.virtualFileSystem.fileType.FileTypeRegistry;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

/**
 * Accepts the file types of CSS and of every language built on it. The dialects live in other modules, SCSS among them,
 * so their file types are looked up among the registered ones instead of being named here.
 */
final class CssIndexInputFilter implements FileBasedIndex.FileTypeSpecificInputFilter {
    private final FileTypeRegistry myFileTypeRegistry;

    CssIndexInputFilter(FileTypeRegistry fileTypeRegistry) {
        myFileTypeRegistry = fileTypeRegistry;
    }

    @Override
    public void registerFileTypesUsedForIndexing(Consumer<FileType> fileTypeSink) {
        for (FileType fileType : myFileTypeRegistry.getRegisteredFileTypes()) {
            if (isCssFileType(fileType)) {
                fileTypeSink.accept(fileType);
            }
        }
    }

    @Override
    public boolean acceptInput(@Nullable Project project, VirtualFile file) {
        return isCssFileType(file.getFileType());
    }

    private static boolean isCssFileType(FileType fileType) {
        return fileType instanceof LanguageFileType languageFileType && languageFileType.getLanguage().isKindOf(CssLanguage.INSTANCE);
    }
}
