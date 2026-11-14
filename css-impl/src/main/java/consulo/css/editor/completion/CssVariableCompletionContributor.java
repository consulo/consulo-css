package consulo.css.editor.completion;

import consulo.annotation.component.ExtensionImpl;
import consulo.css.lang.CssLanguage;
import consulo.css.lang.index.CssCustomPropertyIndex;
import consulo.css.lang.psi.CssVariableReference;
import consulo.language.Language;
import consulo.language.editor.completion.CompletionContributor;
import consulo.language.editor.completion.CompletionInitializationContext;
import consulo.language.editor.completion.CompletionType;
import consulo.language.editor.completion.lookup.LookupElementBuilder;
import consulo.language.icon.IconDescriptorUpdaters;
import consulo.language.pattern.StandardPatterns;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.language.psi.stub.FileBasedIndex;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.platform.base.icon.PlatformIconGroup;
import consulo.project.Project;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author VISTALL
 * @since 2024-03-16
 */
@ExtensionImpl
public class CssVariableCompletionContributor extends CompletionContributor {
    public CssVariableCompletionContributor() {
        extend(
            CompletionType.BASIC,
            StandardPatterns.psiElement().withParent(CssVariableReference.class),
            (parameters, context, result) -> {
                CssVariableReference reference = PsiTreeUtil.getParentOfType(parameters.getPosition(), CssVariableReference.class);

                assert reference != null;

                Set<String> names = new HashSet<>();
                reference.visitVariants(element -> {
                    String name = element.getName();
                    if (name == null || !names.add(name)) {
                        return;
                    }
                    result.accept(LookupElementBuilder.create(name).withIcon(IconDescriptorUpdaters.getIcon(element, 0)));
                });

                // the custom properties of the other stylesheets, with their value
                Project project = reference.getProject();
                GlobalSearchScope scope = GlobalSearchScope.projectScope(project);
                FileBasedIndex index = FileBasedIndex.getInstance();
                for (String name : index.getAllKeys(CssCustomPropertyIndex.INDEX_ID, project)) {
                    if (names.contains(name) || name.endsWith("*")) {
                        continue;
                    }
                    List<CssCustomPropertyIndex.Declaration> declarations = CssCustomPropertyIndex.getDeclarations(name, scope);
                    if (!declarations.isEmpty() && names.add(name)) {
                        result.accept(LookupElementBuilder.create(name)
                            .withIcon(PlatformIconGroup.nodesVariable())
                            .withTypeText(declarations.get(0).value(), true));
                    }
                }
            }
        );
    }

    @Override
    public void beforeCompletion(CompletionInitializationContext context) {
        context.setDummyIdentifier("--dummy-completion-var");
    }

    @Override
    public Language getLanguage() {
        return CssLanguage.INSTANCE;
    }
}
