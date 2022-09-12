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

package consulo.xstylesheet.table;

import consulo.annotation.component.ExtensionImpl;
import consulo.application.util.NotNullLazyValue;
import consulo.util.io.FileUtil;
import consulo.util.lang.StringUtil;
import consulo.xstylesheet.definition.XStyleSheetTable;
import consulo.xstylesheet.definition.XStyleSheetTableProvider;
import consulo.xstylesheet.definition.impl.EmptyXStyleSheetTable;
import consulo.xstylesheet.psi.XStyleSheetFile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author VISTALL
 * @since 2020-08-08
 */
@ExtensionImpl(order = "last")
public class UnparsedXStyleSheetTableProvider implements XStyleSheetTableProvider
{
	private NotNullLazyValue<XStyleSheetTable> myTable = NotNullLazyValue.createValue(this::initialize);

	private XStyleSheetTable initialize()
	{
		try
		{
			InputStream inputStream = getClass().getResourceAsStream("/consulo/xstylesheet/unparsedProperties.txt");

			String lines = FileUtil.loadTextAndClose(inputStream);

			String[] properties = StringUtil.splitByLines(lines, true);

			return new UnparsedXStyleSheetTable(properties);
		}
		catch(IOException ignored)
		{
			ignored.printStackTrace();
		}

		return EmptyXStyleSheetTable.INSTANCE;
	}

	@Nullable
	@Override
	public XStyleSheetTable getTableForFile(@Nonnull XStyleSheetFile file)
	{
		return myTable.get();
	}
}
