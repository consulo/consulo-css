package consulo.xstylesheet.psi;

import consulo.annotation.access.RequiredReadAction;
import consulo.language.psi.PsiElement;
import consulo.xstylesheet.psi.reference.nameResolving.XStyleRuleCondition;

import org.jspecify.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author VISTALL
 * @since 2017-08-19
 */
public interface XStyleSheetRoot extends PsiElement {
    @Nullable
    @RequiredReadAction
    default PsiXStyleSheetRule findRule(XStyleRuleCondition condition) {
        for (PsiXStyleSheetRule o : getRules()) {
            for (XStyleSheetSelector reference : o.getSelectors()) {
                if (condition.isAccepted(reference)) {
                    return o;
                }
            }
        }
        return null;
    }

    @RequiredReadAction
    default List<PsiXStyleSheetRule> findRules(XStyleRuleCondition condition) {
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

    @RequiredReadAction
    PsiXStyleSheetRule[] getRules();
}
