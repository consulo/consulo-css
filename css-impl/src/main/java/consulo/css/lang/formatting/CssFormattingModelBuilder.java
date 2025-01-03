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

package consulo.css.lang.formatting;

import consulo.annotation.component.ExtensionImpl;
import consulo.css.lang.CssLanguage;
import consulo.language.Language;
import consulo.language.codeStyle.*;
import consulo.language.psi.PsiFile;

import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 2013-07-08
 */
@ExtensionImpl
public class CssFormattingModelBuilder implements FormattingModelBuilder {
    @Nonnull
    @Override
    public FormattingModel createModel(@Nonnull FormattingContext formattingContext) {
        PsiFile file = formattingContext.getContainingFile();
        FormattingDocumentModel model = FormattingDocumentModel.create(file);
        Block rootBlock = new CssFormattingBlock(formattingContext.getNode(), null, null);
        return new PsiBasedFormattingModel(file, rootBlock, model);
    }

    @Nonnull
    @Override
    public Language getLanguage() {
        return CssLanguage.INSTANCE;
    }
}
