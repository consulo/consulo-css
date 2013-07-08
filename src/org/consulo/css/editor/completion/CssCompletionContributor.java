package org.consulo.css.editor.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.Function;
import com.intellij.util.ProcessingContext;
import org.consulo.css.lang.psi.CssFile;
import org.consulo.xstylesheet.definition.XStyleSheetProperty;
import org.consulo.xstylesheet.definition.XStyleSheetPropertyValueEntry;
import org.consulo.xstylesheet.definition.XStyleSheetPropertyValuePart;
import org.consulo.xstylesheet.definition.XStyleSheetTable;
import org.consulo.xstylesheet.psi.PsiXStyleSheetProperty;
import org.consulo.xstylesheet.psi.PsiXStyleSheetPropertyValuePart;
import org.consulo.xstylesheet.psi.PsiXStyleSheetRule;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class CssCompletionContributor extends CompletionContributor {
  public CssCompletionContributor() {
    extend(CompletionType.BASIC, PlatformPatterns.psiElement().withParent(PsiXStyleSheetProperty.class), new CompletionProvider<CompletionParameters>() {
      @Override
      protected void addCompletions(@NotNull CompletionParameters completionParameters, ProcessingContext processingContext, @NotNull CompletionResultSet completionResultSet) {
        PsiXStyleSheetRule rule = PsiTreeUtil.getParentOfType(completionParameters.getPosition(), PsiXStyleSheetRule.class);
        if(rule == null) {
          return;
        }

        CssFile containingFile = (CssFile) completionParameters.getOriginalFile();

        List<String> alreadyExists = new ArrayList<String>();
        for (PsiXStyleSheetProperty property : rule.getProperties()) {
          alreadyExists.add(property.getName());
        }

        XStyleSheetTable xStyleSheetTable = containingFile.getXStyleSheetTable();
        for (XStyleSheetProperty property : xStyleSheetTable.getProperties()) {
          if(alreadyExists.contains(property.getName())) {
            continue;
          }


          String defaultText = StringUtil.join(property.getInitialEntries(), new Function<XStyleSheetPropertyValueEntry, String>() {
            @Override
            public String fun(XStyleSheetPropertyValueEntry entry) {
              XStyleSheetPropertyValuePart[] parts = entry.getParts();

              if(parts.length > 0) {
                return parts[0].getValue();
              }
              return "";
            }
          }, ", ");

          StringBuilder b = new StringBuilder(property.getName());
          if(!defaultText.isEmpty()) {
            b.append(": ");
            b.append(defaultText);
            b.append(";");
          }

          LookupElementBuilder builder = LookupElementBuilder.create(b.toString());
          builder = builder.withPresentableText(property.getName());
          if(!defaultText.isEmpty()) {
            builder = builder.withTypeText(defaultText, true);
          }
          completionResultSet.addElement(builder);
        }
      }
    });

    extend(CompletionType.BASIC, PlatformPatterns.psiElement().withParent(PsiXStyleSheetPropertyValuePart.class), new CompletionProvider<CompletionParameters>() {
      @Override
      protected void addCompletions(@NotNull CompletionParameters completionParameters, ProcessingContext processingContext, @NotNull CompletionResultSet completionResultSet) {

        PsiXStyleSheetPropertyValuePart parent = (PsiXStyleSheetPropertyValuePart) completionParameters.getPosition().getParent();
        if(parent == null) {
          return;
        }

        for (XStyleSheetPropertyValuePart o : parent.getValueParts()) {
          completionResultSet.addAllElements(o.getLookupElements());
        }
      }
    });
  }
}
