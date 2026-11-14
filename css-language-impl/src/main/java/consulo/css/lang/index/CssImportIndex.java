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
import consulo.css.lang.psi.CssFunctionCall;
import consulo.css.lang.psi.CssImport;
import consulo.css.lang.CssTokens;
import consulo.index.io.DataIndexer;
import consulo.index.io.EnumeratorStringDescriptor;
import consulo.index.io.ID;
import consulo.index.io.KeyDescriptor;
import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiRecursiveElementWalkingVisitor;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.language.psi.stub.FileBasedIndex;
import consulo.language.psi.stub.FileContent;
import consulo.language.psi.stub.ScalarIndexExtension;
import consulo.util.lang.StringUtil;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.fileType.FileTypeRegistry;
import jakarta.inject.Inject;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * The paths imported by {@code @import} rules in CSS files and in the files of every language built on CSS. A key is
 * the path as written, without quotes: {@code 'tailwindcss'} and {@code url("theme.css")} give {@code tailwindcss} and
 * {@code theme.css}. The index stores no values.
 *
 * @since 2026-11-06
 */
@ExtensionImpl
public class CssImportIndex extends ScalarIndexExtension<String> {
    public static final ID<String, Void> INDEX_ID = ID.create("css.import.index");

    private final FileBasedIndex.InputFilter myInputFilter;

    @Inject
    public CssImportIndex(FileTypeRegistry fileTypeRegistry) {
        myInputFilter = new CssIndexInputFilter(fileTypeRegistry);
    }

    /**
     * Returns the files in {@code scope} importing {@code path}, given as written in the {@code @import} rule
     */
    @RequiredReadAction
    public static Collection<VirtualFile> getFilesImporting(String path, GlobalSearchScope scope) {
        return FileBasedIndex.getInstance().getContainingFiles(INDEX_ID, path, scope);
    }

    /**
     * Returns the imported path of {@code rule}, without quotes, or {@code null} when it has none
     */
    @RequiredReadAction
    public static @Nullable String getImportedPath(CssImport rule) {
        for (PsiElement child = rule.getFirstChild(); child != null; child = child.getNextSibling()) {
            ASTNode node = child.getNode();
            if (node != null && node.getElementType() == CssTokens.STRING) {
                return StringUtil.unquoteString(child.getText());
            }
            if (child instanceof CssFunctionCall call) {
                String text = call.getText();
                if (!StringUtil.startsWithIgnoreCase(text, "url(") || !text.endsWith(")")) {
                    return null;
                }
                return StringUtil.unquoteString(text.substring(4, text.length() - 1).trim());
            }
        }
        return null;
    }

    @Override
    public ID<String, Void> getName() {
        return INDEX_ID;
    }

    @Override
    public DataIndexer<String, Void, FileContent> getIndexer() {
        return CssImportIndex::collectImports;
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

    private static Map<String, Void> collectImports(FileContent inputData) {
        Map<String, Void> result = new HashMap<>();
        PsiFile file = inputData.getPsiFile();
        file.accept(new PsiRecursiveElementWalkingVisitor() {
            @Override
            @RequiredReadAction
            public void visitElement(PsiElement element) {
                if (element instanceof CssImport rule) {
                    String path = getImportedPath(rule);
                    if (path != null && !path.isEmpty()) {
                        result.put(path, null);
                    }
                    return;
                }
                super.visitElement(element);
            }
        });
        return result;
    }
}
