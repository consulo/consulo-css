package consulo.xstylesheet.psi;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
	default PsiXStyleSheetRule findRule(@Nonnull XStyleRuleCondition condition)
	{
		for(PsiXStyleSheetRule o : getRules())
		{
			for(XStyleSheetSelector reference : o.getSelectors())
			{
				if(condition.isAccepted(reference))
				{
					return o;
				}
			}
		}
		return null;
	}

	@Nonnull
	@RequiredReadAction
	default List<PsiXStyleSheetRule> findRules(@Nonnull XStyleRuleCondition condition)
	{
		List<PsiXStyleSheetRule> list = new ArrayList<>();
		for(PsiXStyleSheetRule o : getRules())
		{
			for(XStyleSheetSelector reference : o.getSelectors())
			{
				if(condition.isAccepted(reference))
				{
					list.add(o);
				}
			}
		}
		return list;
	}

	@Nonnull
	@RequiredReadAction
	PsiXStyleSheetRule[] getRules();
}
