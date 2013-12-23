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

package org.consulo.css.lang;

import javax.swing.Icon;

import org.consulo.css.CssIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.fileTypes.LanguageFileType;

/**
 * @author VISTALL
 * @since 23:57/12.06.13
 */
public class CssFileType extends LanguageFileType {
    public static final CssFileType INSTANCE = new CssFileType();

    private CssFileType() {
        super(CssLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "CSS";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "CSS";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "css";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return CssIcons.CssFile;
    }
}
