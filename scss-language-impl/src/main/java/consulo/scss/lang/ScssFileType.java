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

package consulo.scss.lang;

import consulo.language.file.LanguageFileType;
import consulo.localize.LocalizeValue;
import consulo.scss.lang.icon.ScssIconGroup;
import consulo.ui.image.Image;

import org.jspecify.annotations.Nullable;

/**
 * @author VISTALL
 * @since 2026-03-16
 */
public class ScssFileType extends LanguageFileType {
    public static final ScssFileType INSTANCE = new ScssFileType();

    private ScssFileType() {
        super(ScssLanguage.INSTANCE);
    }

    @Override
    public String getId() {
        return "SCSS";
    }

    @Override
    public LocalizeValue getDescription() {
        return LocalizeValue.localizeTODO("SCSS");
    }

    @Override
    public String getDefaultExtension() {
        return "scss";
    }

    @Nullable
    @Override
    public Image getIcon() {
        return ScssIconGroup.sass();
    }
}
