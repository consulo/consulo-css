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
import consulo.css.lang.CssLanguage;
import consulo.index.io.DataIndexer;
import consulo.index.io.EnumeratorStringDescriptor;
import consulo.index.io.ID;
import consulo.index.io.KeyDescriptor;
import consulo.language.file.LanguageFileType;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiManager;
import consulo.language.psi.PsiRecursiveElementWalkingVisitor;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.language.psi.stub.FileBasedIndex;
import consulo.language.psi.stub.FileContent;
import consulo.language.psi.stub.ScalarIndexExtension;
import consulo.project.Project;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.fileType.FileType;
import consulo.virtualFileSystem.fileType.FileTypeRegistry;
import consulo.xstylesheet.psi.XStyleSheetSimpleSelector;
import consulo.xstylesheet.psi.XStyleSheetSimpleSelectorType;
import jakarta.inject.Inject;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * The names of the classes declared by class selectors ({@code .name}) in CSS files and in the files of every
 * language built on CSS, such as SCSS. A key is a class name without its leading dot; the index stores no values.
 * <p>
 * Only names that appear literally in a selector are indexed. A class name assembled by a preprocessor, such as
 * {@code &-suffix} nested in an SCSS rule, is not.
 *
 * @author VISTALL
 * @since 2026-10-18
 */
@ExtensionImpl
public class CssClassIndex extends ScalarIndexExtension<String> {
    public static final ID<String, Void> INDEX_ID = ID.create("css.class.index");

    private final FileBasedIndex.InputFilter myInputFilter;

    @Inject
    public CssClassIndex(FileTypeRegistry fileTypeRegistry) {
        myInputFilter = new CssInputFilter(fileTypeRegistry);
    }

    /**
     * Returns the names of the classes declared in {@code scope}. The keys of the whole project are checked one by one,
     * because a key outlives its last declaration until the index is compacted and may come from outside the scope.
     */
    @RequiredReadAction
    public static Set<String> getAllClassNames(Project project, GlobalSearchScope scope) {
        FileBasedIndex index = FileBasedIndex.getInstance();
        Set<String> result = new HashSet<>();
        for (String className : index.getAllKeys(INDEX_ID, project)) {
            // the processor stops at the first file, so processValues returns false only when such a file exists
            if (!index.processValues(INDEX_ID, className, null, (file, value) -> false, scope)) {
                result.add(className);
            }
        }
        return result;
    }

    /**
     * Returns the files in {@code scope} declaring the class {@code className}, given without its leading dot.
     */
    @RequiredReadAction
    public static Collection<VirtualFile> getFilesWithClass(String className, GlobalSearchScope scope) {
        return FileBasedIndex.getInstance().getContainingFiles(INDEX_ID, className, scope);
    }

    /**
     * Returns the class selectors in {@code scope} declaring the class {@code className}, given without its leading dot.
     * Only the files found by the index are parsed.
     */
    @RequiredReadAction
    public static List<XStyleSheetSimpleSelector> findClassSelectors(Project project, String className, GlobalSearchScope scope) {
        PsiManager psiManager = PsiManager.getInstance(project);
        List<XStyleSheetSimpleSelector> result = new ArrayList<>();
        for (VirtualFile file : getFilesWithClass(className, scope)) {
            PsiFile psiFile = psiManager.findFile(file);
            if (psiFile == null) {
                continue;
            }

            visitClassSelectors(psiFile, (selector, name) -> {
                if (name.equals(className)) {
                    result.add(selector);
                }
            });
        }
        return result;
    }

    @Override
    public ID<String, Void> getName() {
        return INDEX_ID;
    }

    @Override
    public DataIndexer<String, Void, FileContent> getIndexer() {
        return CssClassIndex::collectClassNames;
    }

    @Override
    public KeyDescriptor<String> getKeyDescriptor() {
        return EnumeratorStringDescriptor.INSTANCE;
    }

    @Override
    public FileBasedIndex.InputFilter getInputFilter() {
        return myInputFilter;
    }

    @Override
    public boolean dependsOnFileContent() {
        return true;
    }

    @Override
    public int getVersion() {
        return 1;
    }

    private static Map<String, Void> collectClassNames(FileContent inputData) {
        Map<String, Void> result = new HashMap<>();
        visitClassSelectors(inputData.getPsiFile(), (selector, name) -> result.put(name, null));
        return result;
    }

    /**
     * Class selectors are not lexer tokens: the parser joins a dot and the identifier after it, so they are found in the
     * PSI. Scanning the tokens would also take the dots inside property values for selectors.
     */
    private static void visitClassSelectors(PsiFile file, BiConsumer<XStyleSheetSimpleSelector, String> consumer) {
        file.accept(new PsiRecursiveElementWalkingVisitor() {
            @Override
            @RequiredReadAction
            public void visitElement(PsiElement element) {
                if (element instanceof XStyleSheetSimpleSelector selector && selector.getType() == XStyleSheetSimpleSelectorType.CLASS) {
                    String name = selector.getName();
                    if (name != null && !name.isEmpty()) {
                        consumer.accept(selector, name);
                    }
                }
                super.visitElement(element);
            }
        });
    }

    private static boolean isCssFileType(FileType fileType) {
        return fileType instanceof LanguageFileType languageFileType && languageFileType.getLanguage().isKindOf(CssLanguage.INSTANCE);
    }

    /**
     * Accepts the file types of CSS and of every language built on it. The dialects live in other modules, SCSS among them,
     * so their file types are looked up among the registered ones instead of being named here.
     */
    private static final class CssInputFilter implements FileBasedIndex.FileTypeSpecificInputFilter {
        private final FileTypeRegistry myFileTypeRegistry;

        private CssInputFilter(FileTypeRegistry fileTypeRegistry) {
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
    }
}
