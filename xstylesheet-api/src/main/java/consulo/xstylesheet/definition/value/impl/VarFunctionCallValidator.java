package consulo.xstylesheet.definition.value.impl;

import consulo.annotation.access.RequiredReadAction;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.rawHighlight.HighlightInfo;
import consulo.language.editor.rawHighlight.HighlightInfoType;
import consulo.xstylesheet.definition.XStyleSheetFunctionCallDescriptor;
import consulo.xstylesheet.highlight.XStyleSheetColors;
import consulo.xstylesheet.psi.PsiXStyleSheetFunctionCall;

import jakarta.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author VISTALL
 * @since 2024-03-14
 */
@ExtensionImpl
public class VarFunctionCallValidator implements XStyleSheetFunctionCallDescriptor {
    public static final String VAR_NAME = "var";

    @RequiredReadAction
    @Override
    public boolean isMyFunction(PsiXStyleSheetFunctionCall functionCall) {
        return functionCall.getCallName().equals(VAR_NAME);
    }

    @RequiredReadAction
    @Override
    public List<HighlightInfo> createHighlights(@Nonnull PsiXStyleSheetFunctionCall functionCall) {
        List<HighlightInfo> list = new ArrayList<>();
        list.add(HighlightInfo.newHighlightInfo(HighlightInfoType.INFORMATION)
            .range(functionCall.getCallElement())
            .textAttributes(XStyleSheetColors.KEYWORD)
            .create());
        return list;
    }
}
