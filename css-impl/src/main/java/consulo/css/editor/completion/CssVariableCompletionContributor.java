package consulo.css.editor.completion;

import consulo.annotation.component.ExtensionImpl;
import consulo.css.lang.CssLanguage;
import consulo.css.lang.psi.CssVariableReference;
import consulo.language.Language;
import consulo.language.editor.completion.CompletionContributor;
import consulo.language.editor.completion.CompletionInitializationContext;
import consulo.language.editor.completion.CompletionType;
import consulo.language.editor.completion.lookup.LookupElementBuilder;
import consulo.language.icon.IconDescriptorUpdaters;
import consulo.language.pattern.StandardPatterns;
import consulo.language.psi.util.PsiTreeUtil;
import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 2024-03-16
 */
@ExtensionImpl
public class CssVariableCompletionContributor extends CompletionContributor
{
	public CssVariableCompletionContributor()
	{
		extend(CompletionType.BASIC, StandardPatterns.psiElement().withParent(CssVariableReference.class), (parameters, context, result) ->
		{
			CssVariableReference reference = PsiTreeUtil.getParentOfType(parameters.getPosition(), CssVariableReference.class);

			assert reference != null;

			reference.visitVariants(element ->
			{
				String name = element.getName();
				if(name == null)
				{
					return;
				}
				result.accept(LookupElementBuilder.create(name).withIcon(IconDescriptorUpdaters.getIcon(element, 0)));
			});
		});
	}

	@Override
	public void beforeCompletion(@Nonnull CompletionInitializationContext context)
	{
		context.setDummyIdentifier("--dummy-completion-var");
	}

	@Nonnull
	@Override
	public Language getLanguage()
	{
		return CssLanguage.INSTANCE;
	}
}
