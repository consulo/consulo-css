/*
 * Copyright 2013 must-be.org
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
package consulo.xstylesheet.codeInsight;

import consulo.annotation.access.RequiredReadAction;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.inspection.LocalInspectionTool;
import consulo.language.editor.inspection.ProblemHighlightType;
import consulo.language.editor.inspection.ProblemsHolder;
import consulo.language.editor.rawHighlight.HighlightDisplayLevel;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiElementVisitor;
import consulo.localize.LocalizeValue;
import consulo.xstylesheet.definition.XStyleSheetProperty;
import consulo.xstylesheet.psi.PsiXStyleSheetProperty;
import consulo.xstylesheet.psi.PsiXStyleSheetVariable;

import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 2013-07-03
 */
@ExtensionImpl
public class PropertyIsNotValidInspection extends LocalInspectionTool {
    @Nonnull
    @Override
    public PsiElementVisitor buildVisitor(@Nonnull final ProblemsHolder holder, boolean isOnTheFly) {
        return new PsiElementVisitor() {
            @Override
            @RequiredReadAction
            public void visitElement(PsiElement element) {
                if (element instanceof PsiXStyleSheetVariable) {
                    return;
                }

                if (element instanceof PsiXStyleSheetProperty property) {
                    XStyleSheetProperty xStyleSheetProperty = property.getXStyleSheetProperty();
                    if (xStyleSheetProperty == null) {
                        PsiElement nameIdentifier = property.getNameIdentifier();
                        if (nameIdentifier == null) {
                            return;
                        }
                        holder.newProblem(LocalizeValue.of("Invalid property name"))
                            .range(nameIdentifier)
                            .highlightType(ProblemHighlightType.GENERIC_ERROR_OR_WARNING)
                            .create();
                    }
                }
            }
        };
    }

    @Nonnull
    @Override
    public LocalizeValue getGroupDisplayName() {
        return LocalizeValue.localizeTODO("CSS");
    }

    @Nonnull
    @Override
    public LocalizeValue getDisplayName() {
        return LocalizeValue.localizeTODO("Invalid property name");
    }

    @Nonnull
    @Override
    public HighlightDisplayLevel getDefaultLevel() {
        return HighlightDisplayLevel.ERROR;
    }
}
