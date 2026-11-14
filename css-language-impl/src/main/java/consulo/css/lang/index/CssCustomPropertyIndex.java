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
import consulo.css.lang.CssTokens;
import consulo.css.lang.psi.CssAtRule;
import consulo.css.lang.psi.CssVariable;
import consulo.index.io.DataIndexer;
import consulo.index.io.EnumeratorStringDescriptor;
import consulo.index.io.ID;
import consulo.index.io.KeyDescriptor;
import consulo.index.io.data.DataExternalizer;
import consulo.index.io.data.IOUtil;
import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiRecursiveElementWalkingVisitor;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.language.psi.stub.FileBasedIndex;
import consulo.language.psi.stub.FileBasedIndexExtension;
import consulo.language.psi.stub.FileContent;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.virtualFileSystem.fileType.FileTypeRegistry;
import jakarta.inject.Inject;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The custom properties ({@code --name: value}) declared in CSS files and in the files of every language built on CSS.
 * A key is the property name with its dashes; a value lists the declarations of the file, each with the name of the
 * at-rule it is declared in, without the {@code @} ({@code theme} for Tailwind's {@code @theme}, an empty string in a
 * style rule such as {@code :root}), and its value as written.
 *
 * @since 2026-11-06
 */
@ExtensionImpl
public class CssCustomPropertyIndex extends FileBasedIndexExtension<String, List<CssCustomPropertyIndex.Declaration>> {
    public static final ID<String, List<Declaration>> INDEX_ID = ID.create("css.custom.property.index");

    /**
     * @param atRule the name of the enclosing at-rule without {@code @}, or an empty string
     * @param value  the value as written, trimmed
     */
    public record Declaration(String atRule, String value) {
    }

    private final FileBasedIndex.InputFilter myInputFilter;

    @Inject
    public CssCustomPropertyIndex(FileTypeRegistry fileTypeRegistry) {
        myInputFilter = new CssIndexInputFilter(fileTypeRegistry);
    }

    /**
     * Returns the declarations of {@code name}, with its dashes, in {@code scope}
     */
    @RequiredReadAction
    public static List<Declaration> getDeclarations(String name, GlobalSearchScope scope) {
        List<Declaration> result = new ArrayList<>();
        for (List<Declaration> declarations : FileBasedIndex.getInstance().getValues(INDEX_ID, name, scope)) {
            result.addAll(declarations);
        }
        return result;
    }

    /**
     * Returns the name of the at-rule {@code element} is declared in, without {@code @}, or an empty string
     */
    @RequiredReadAction
    public static String getEnclosingAtRuleName(PsiElement element) {
        CssAtRule atRule = PsiTreeUtil.getParentOfType(element, CssAtRule.class);
        PsiElement keyword = atRule == null ? null : atRule.getFirstChild();
        String text = keyword == null ? "" : keyword.getText();
        return text.startsWith("@") ? text.substring(1) : "";
    }

    @Override
    public ID<String, List<Declaration>> getName() {
        return INDEX_ID;
    }

    @Override
    public DataIndexer<String, List<Declaration>, FileContent> getIndexer() {
        return CssCustomPropertyIndex::collectDeclarations;
    }

    @Override
    public KeyDescriptor<String> getKeyDescriptor() {
        return EnumeratorStringDescriptor.INSTANCE;
    }

    @Override
    public DataExternalizer<List<Declaration>> getValueExternalizer() {
        return new DeclarationsExternalizer();
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

    private static Map<String, List<Declaration>> collectDeclarations(FileContent inputData) {
        Map<String, List<Declaration>> result = new HashMap<>();
        PsiFile file = inputData.getPsiFile();
        file.accept(new PsiRecursiveElementWalkingVisitor() {
            @Override
            @RequiredReadAction
            public void visitElement(PsiElement element) {
                if (element instanceof CssVariable variable) {
                    String name = variable.getName();
                    if (name != null && name.startsWith("--")) {
                        result.computeIfAbsent(name, key -> new ArrayList<>())
                            .add(new Declaration(getEnclosingAtRuleName(variable), getValueText(variable)));
                    }
                    return;
                }
                super.visitElement(element);
            }
        });
        return result;
    }

    /**
     * The text between the colon and the semicolon, or the end of the declaration
     */
    @RequiredReadAction
    private static String getValueText(CssVariable variable) {
        ASTNode node = variable.getNode();
        ASTNode colon = node.findChildByType(CssTokens.COLON);
        if (colon == null) {
            return "";
        }
        int start = colon.getStartOffset() + colon.getTextLength() - node.getStartOffset();
        ASTNode semicolon = node.findChildByType(CssTokens.SEMICOLON);
        int end = semicolon == null ? node.getTextLength() : semicolon.getStartOffset() - node.getStartOffset();
        return node.getText().substring(start, end).trim();
    }

    private static final class DeclarationsExternalizer implements DataExternalizer<List<Declaration>> {
        @Override
        public void save(DataOutput out, List<Declaration> value) throws IOException {
            out.writeInt(value.size());
            for (Declaration declaration : value) {
                IOUtil.writeUTF(out, declaration.atRule());
                IOUtil.writeUTF(out, declaration.value());
            }
        }

        @Override
        public List<Declaration> read(DataInput in) throws IOException {
            int size = in.readInt();
            List<Declaration> result = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                result.add(new Declaration(IOUtil.readUTF(in), IOUtil.readUTF(in)));
            }
            return result;
        }
    }
}
