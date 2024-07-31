/*
 * Copyright 2013-2022 consulo.io
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

package consulo.xstylesheet.definition;

import consulo.annotation.access.RequiredReadAction;
import consulo.annotation.component.ComponentScope;
import consulo.annotation.component.ExtensionAPI;
import consulo.component.extension.ExtensionPointName;
import consulo.language.editor.rawHighlight.HighlightInfo;
import consulo.xstylesheet.psi.PsiXStyleSheetFunctionCall;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author VISTALL
 * @since 2022-09-11
 */
@ExtensionAPI(ComponentScope.APPLICATION)
public interface XStyleSheetFunctionCallDescriptor {
    @RequiredReadAction
    boolean isMyFunction(PsiXStyleSheetFunctionCall functionCall);

    @RequiredReadAction
    List<HighlightInfo> createHighlights(@Nonnull PsiXStyleSheetFunctionCall functionCall);
}
