/*
 * Copyright 2013-2020 consulo.io
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

import com.intellij.openapi.extensions.ExtensionPointName;
import consulo.xstylesheet.psi.XStyleSheetFile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author VISTALL
 * @since 2020-08-08
 */
public interface XStyleSheetTableProvider
{
	ExtensionPointName<XStyleSheetXmlTableProvider> EP_NAME = ExtensionPointName.create("consulo.xstylesheet.tableProvider");

	@Nullable
	XStyleSheetTable getTableForFile(@Nonnull XStyleSheetFile file);
}