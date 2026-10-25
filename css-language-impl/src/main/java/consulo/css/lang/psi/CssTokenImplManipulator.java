/*
 * Copyright 2013-2026 consulo.io
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

package consulo.css.lang.psi;

import consulo.annotation.component.ExtensionImpl;
import consulo.document.util.TextRange;
import consulo.language.psi.AbstractElementManipulator;
import consulo.language.util.IncorrectOperationException;

/**
 * Lets references contributed to CSS identifiers rename them.
 *
 * @since 2026-10-25
 */
@ExtensionImpl
public class CssTokenImplManipulator extends AbstractElementManipulator<CssTokenImpl> {
    @Override
    public CssTokenImpl handleContentChange(CssTokenImpl element, TextRange range, String newContent) throws IncorrectOperationException {
        String newText = range.replace(element.getText(), newContent);
        return (CssTokenImpl) element.replaceWithText(newText);
    }

    @Override
    public Class<CssTokenImpl> getElementClass() {
        return CssTokenImpl.class;
    }
}
