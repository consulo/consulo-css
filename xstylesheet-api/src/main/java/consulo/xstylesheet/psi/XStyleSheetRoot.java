package consulo.xstylesheet.psi;

import consulo.annotation.access.RequiredReadAction;
import consulo.language.psi.PsiElement;
import consulo.xstylesheet.psi.reference.nameResolving.XStyleRuleCondition;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author VISTALL
 * @since 2017-08-19
 */
public interface XStyleSheetRoot extends PsiElement {
    @Nullable
    @RequiredReadAction
    default PsiXStyleSheetRule findRule(@Nonnull XStyleRuleCondition condition) {
        for (PsiXStyleSheetRule o : getRules()) {
            for (XStyleSheetSelector reference : o.getSelectors()) {
                if (condition.isAccepted(reference)) {
                    return o;
                }
            }
        }
        return null;
    }

    @Nonnull
    @RequiredReadAction
    default List<PsiXStyleSheetRule> findRules(@Nonnull XStyleRuleCondition condition) {
        List<PsiXStyleSheetRule> list = new ArrayList<>();
        for (PsiXStyleSheetRule o : getRules()) {
            for (XStyleSheetSelector reference : o.getSelectors()) {
                if (condition.isAccepted(reference)) {
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
