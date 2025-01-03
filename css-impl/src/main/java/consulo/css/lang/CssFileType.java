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

package consulo.css.lang;

import consulo.css.icon.CssIconGroup;
import consulo.language.file.LanguageFileType;
import consulo.localize.LocalizeValue;
import consulo.ui.image.Image;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * @author VISTALL
 * @since 2013-06-12
 */
public class CssFileType extends LanguageFileType {
    public static final CssFileType INSTANCE = new CssFileType();

    private CssFileType() {
        super(CssLanguage.INSTANCE);
    }

    @Nonnull
    @Override
    public String getId() {
        return "CSS";
    }

    @Nonnull
    @Override
    public LocalizeValue getDescription() {
        return LocalizeValue.localizeTODO("CSS");
    }

    @Nonnull
    @Override
    public String getDefaultExtension() {
        return "css";
    }

    @Nullable
    @Override
    public Image getIcon() {
        return CssIconGroup.cssfile();
    }
}
