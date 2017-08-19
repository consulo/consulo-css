package consulo.xstylesheet.psi;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.psi.PsiElement;
import consulo.annotations.RequiredReadAction;
import consulo.xstylesheet.psi.reference.nameResolving.XStyleRuleCondition;

/**
 * @author VISTALL
 * @since 19-Aug-17
 */
public interface XStyleSheetRoot extends PsiElement
{
	@Nullable
	@RequiredReadAction
	default PsiXStyleSheetRule findRule(@NotNull XStyleRuleCondition condition)
	{
		for(PsiXStyleSheetRule o : getRules())
		{
			for(PsiXStyleSheetSelectorDeclaration reference : o.getSelectorDeclarations())
			{
				if(condition.isAccepted(reference))
				{
					return o;
				}
			}
		}
		return null;
	}

	@NotNull
	@RequiredReadAction
	default List<PsiXStyleSheetRule> findRules(@NotNull XStyleRuleCondition condition)
	{
		List<PsiXStyleSheetRule> list = new ArrayList<>();
		for(PsiXStyleSheetRule o : getRules())
		{
			for(PsiXStyleSheetSelectorDeclaration reference : o.getSelectorDeclarations())
			{
				if(condition.isAccepted(reference))
				{
					list.add(o);
				}
			}
		}
		return list;
	}

	@NotNull
	@RequiredReadAction
	PsiXStyleSheetRule[] getRules();
}
