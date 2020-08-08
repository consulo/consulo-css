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

package consulo.xstylesheet.definition;

import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.util.JDOMUtil;
import com.intellij.openapi.util.NotNullLazyValue;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.ReflectionUtil;
import consulo.logging.Logger;
import consulo.xstylesheet.definition.impl.*;
import consulo.xstylesheet.psi.XStyleSheetFile;
import org.jdom.Document;
import org.jdom.Element;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.InputStream;
import java.util.*;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class XStyleSheetXmlTableProvider implements XStyleSheetTableProvider
{
	private static final Logger LOGGER = Logger.getInstance(XStyleSheetXmlTableProvider.class);

	public static final ExtensionPointName<XStyleSheetXmlTableProvider> EP_NAME = ExtensionPointName.create("consulo.xstylesheet.table");

	private NotNullLazyValue<XStyleSheetTable> myLazyTableValue;

	public XStyleSheetXmlTableProvider(@Nullable String pathToResource)
	{
		myLazyTableValue = NotNullLazyValue.createValue(() ->
		{
			InputStream stream = getClass().getResourceAsStream(pathToResource);
			if(stream == null)
			{
				LOGGER.error("File " + pathToResource + " not found for loader: " + getClass().getClass());
				return EmptyXStyleSheetTable.INSTANCE;
			}
			return loadDocument(stream);
		});
	}

	@Nullable
	@Override
	public XStyleSheetTable getTableForFile(@Nonnull XStyleSheetFile file)
	{
		return myLazyTableValue.get();
	}

	public XStyleSheetTable getTable()
	{
		return myLazyTableValue.getValue();
	}

	@SuppressWarnings("unchecked")
	private XStyleSheetTable loadDocument(InputStream stream)
	{
		try
		{
			Map<String, XStyleSheetPropertyValuePartParser> valueDefinitions = new HashMap<String, XStyleSheetPropertyValuePartParser>();
			List<XStyleSheetProperty> properties = new ArrayList<XStyleSheetProperty>();

			Document document = JDOMUtil.loadDocument(stream);
			for(Element element : document.getRootElement().getChildren())
			{
				String name = element.getName();
				if("value-definition".equals(name))
				{
					String tempName = element.getAttributeValue("name");
					Class<? extends XStyleSheetPropertyValuePartParser> clazz = (Class<? extends XStyleSheetPropertyValuePartParser>) Class.forName(element.getAttributeValue("implClass"), true, getClass().getClassLoader());

					valueDefinitions.put(tempName, ReflectionUtil.createInstance(ReflectionUtil.getDefaultConstructor(clazz)));
				}
				else if("property".equals(name))
				{
					String propertyName = element.getAttributeValue("name");

					List<XStyleSheetPropertyValueEntry> validEntries = parseEntries(valueDefinitions, element.getChild("valid-values"));
					List<XStyleSheetPropertyValueEntry> initialEntries = parseEntries(valueDefinitions, element.getChild("initial-values"));

					properties.add(new XStyleSheetPropertyImpl(propertyName, validEntries, initialEntries));
				}
			}

			return new XStyleSheetTableImpl(properties);
		}
		catch(Exception e)
		{
			LOGGER.error(e);
		}
		return EmptyXStyleSheetTable.INSTANCE;
	}

	private List<XStyleSheetPropertyValueEntry> parseEntries(Map<String, XStyleSheetPropertyValuePartParser> valueDefinitions, Element parent)
	{
		if(parent == null)
		{
			return Collections.emptyList();
		}

		List<XStyleSheetPropertyValueEntry> entries = new ArrayList<XStyleSheetPropertyValueEntry>();
		// entry
		for(Element child : parent.getChildren())
		{
			List<XStyleSheetPropertyValuePart> parts = new ArrayList<XStyleSheetPropertyValuePart>();
			for(Element child2 : child.getChildren())
			{
				XStyleSheetPropertyValuePartParser parser = valueDefinitions.get(child2.getName());

				parts.add(new XStyleSheetPropertyValuePartImpl(parser, StringUtil.nullize(child2.getText(), true)));
			}
			entries.add(new XStyleSheetPropertyValueEntryImpl(parts));
		}
		return entries;
	}
}
